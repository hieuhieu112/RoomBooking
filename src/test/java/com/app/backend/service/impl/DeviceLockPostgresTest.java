package com.app.backend.service.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Learning integration test: real PostgreSQL transactions + production V8 function.
 * Does NOT load Spring, application profiles, repository @Lock, or BookingServiceImpl.
 * The separate BookingDeviceRulesTest checks service decisions, not database locking.
 * Uses a private generated schema, never the application's tables.
 * Opt in to the dev datasource with -Drba.test.useDevDatabase=true, or supply RBA_TEST_DB_URL.
 */
@EnabledIf("databaseConfigured")
class DeviceLockPostgresTest {
    private String schema;
    private ExecutorService workers;
    private String databaseUrl;
    private String databaseUser;
    private String databasePassword;

    static boolean databaseConfigured() {
        return Boolean.getBoolean("rba.test.useDevDatabase")
                || System.getenv("RBA_TEST_DB_URL") != null;
    }

    @BeforeEach
    void setup() throws Exception {
        if (Boolean.getBoolean("rba.test.useDevDatabase")) {
            // Read only datasource properties; do not start Spring Boot, Flyway, or Hibernate.
            var yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(new ClassPathResource("application-dev.yml"));
            var properties = java.util.Objects.requireNonNull(yaml.getObject());
            var environment = new StandardEnvironment();
            databaseUrl = environment.resolveRequiredPlaceholders(properties.getProperty("spring.datasource.url"));
            databaseUser = environment.resolveRequiredPlaceholders(properties.getProperty("spring.datasource.username"));
            databasePassword = environment.resolveRequiredPlaceholders(properties.getProperty("spring.datasource.password"));
        } else {
            databaseUrl = System.getenv("RBA_TEST_DB_URL");
            databaseUser = System.getenv().getOrDefault("RBA_TEST_DB_USER", "postgres");
            databasePassword = System.getenv().getOrDefault("RBA_TEST_DB_PASSWORD", "");
        }
        assertTrue(databaseUrl.startsWith("jdbc:postgresql://"), "A PostgreSQL datasource is required");
        schema = "device_lock_test_" + UUID.randomUUID().toString().replace("-", "");
        workers = Executors.newFixedThreadPool(2);
        try (Connection c = connect(); Statement s = c.createStatement()) {
            s.execute("CREATE SCHEMA " + schema);
            // Deliberately minimal fixture schema; not a test of the application's Flyway history.
            s.execute("CREATE TABLE device (id integer PRIMARY KEY, name varchar, tracking_type varchar, quantity integer, status varchar)");
            s.execute("CREATE TABLE booking (id integer PRIMARY KEY, status integer, start_time timestamp, end_time timestamp)");
            s.execute("CREATE TABLE device_borrow_detail (booking_id integer REFERENCES booking(id), device_id integer REFERENCES device(id), quantity integer)");
            s.execute("INSERT INTO device VALUES (10, 'Projector', 'QUANTITY', 1, 'ACTIVE'), (20, 'Microphone', 'QUANTITY', 1, 'ACTIVE')");
            try (var input = getClass().getResourceAsStream("/db/migration/V8__updateV5.sql")) {
                assertNotNull(input, "Production V8 function must be present");
                s.execute(new String(input.readAllBytes(), StandardCharsets.UTF_8));
            }
        }
    }

    private Connection connect() throws SQLException {
        Connection c = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
        c.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        try (Statement s = c.createStatement()) {
            s.execute("SET search_path TO " + schema);
            s.execute("SET statement_timeout = '10s'");
            s.execute("SET lock_timeout = '8s'");
        }
        return c;
    }

    @AfterEach
    void cleanup() throws Exception {
        if (workers != null) {
            workers.shutdownNow();
            assertTrue(workers.awaitTermination(15, TimeUnit.SECONDS), "Workers did not stop; keeping schema for inspection");
        }
        if (schema != null) {
            try (Connection c = connect(); Statement s = c.createStatement()) {
                // Only this test's generated schema; never public or an application schema.
                s.execute("DROP SCHEMA IF EXISTS " + schema + " CASCADE");
            }
        }
    }

    private void lock(Connection c, int... ids) throws SQLException {
        try (PreparedStatement s = c.prepareStatement(
                "SELECT id FROM device WHERE id = ANY (?) ORDER BY id FOR UPDATE")) {
            Integer[] values = java.util.Arrays.stream(ids).boxed().toArray(Integer[]::new);
            Array array = c.createArrayOf("integer", values);
            try {
                s.setArray(1, array);
                try (ResultSet rows = s.executeQuery()) {
                    int count = 0;
                    while (rows.next()) count++;
                    assertEquals(ids.length, count, "Every requested device must exist");
                }
            } finally {
                array.free();
            }
        }
    }

    private int available(Connection c, int id) throws SQLException {
        try (PreparedStatement s = c.prepareStatement("SELECT available_quantity FROM "
                + "list_device_quantity_available_by_time(timestamp '2030-01-01 09:00', "
                + "ARRAY[?]::integer[], timestamp '2030-01-01 10:00', NULL::integer)")) {
            s.setInt(1, id);
            try (ResultSet rows = s.executeQuery()) {
                assertTrue(rows.next());
                return rows.getInt(1);
            }
        }
    }

    private void borrow(Connection c) throws SQLException {
        try (Statement s = c.createStatement()) {
            s.execute("INSERT INTO booking VALUES (1, 7, timestamp '2030-01-01 09:00', timestamp '2030-01-01 10:00')");
            s.execute("INSERT INTO device_borrow_detail VALUES (1, 10, 1)");
        }
    }

    private int backendPid(Connection c) throws SQLException {
        try (Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT pg_backend_pid()")) {
            r.next();
            return r.getInt(1);
        }
    }

    private void awaitDatabaseBlocking(int blockedPid, int blockerPid) throws Exception {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        try (Connection observer = connect(); PreparedStatement s = observer.prepareStatement(
                "SELECT ? = ANY(pg_blocking_pids(?))")) {
            s.setInt(1, blockerPid);
            s.setInt(2, blockedPid);
            while (System.nanoTime() < deadline) {
                try (ResultSet r = s.executeQuery()) {
                    r.next();
                    if (r.getBoolean(1)) return;
                }
                Thread.sleep(20); // Poll actual database state, not a sleep used to assume ordering.
            }
        }
        fail("PostgreSQL never reported B waiting on A; the test did not establish lock contention");
    }

    private static void await(CountDownLatch latch) throws InterruptedException {
        assertTrue(latch.await(10, TimeUnit.SECONDS), "Timed out waiting for a test signal");
    }

    @Test
    void secondTransactionWaitsThenSeesCommittedBorrowEvenWithReversedIdOrder() throws Exception {
        contention(false);
    }

    @Test
    void rollbackReleasesLocksAndLeavesQuantityAvailable() throws Exception {
        contention(true);
    }

    private void contention(boolean rollbackA) throws Exception {
        CountDownLatch aLocked = new CountDownLatch(1);
        CountDownLatch releaseA = new CountDownLatch(1);
        CompletableFuture<Integer> aPid = new CompletableFuture<>();
        CompletableFuture<Integer> bPid = new CompletableFuture<>();
        Future<?> a = workers.submit(() -> {
            try (Connection c = connect()) {
                c.setAutoCommit(false);
                aPid.complete(backendPid(c));
                try {
                    lock(c, 20, 10);
                    aLocked.countDown();
                    await(releaseA); // A deliberately keeps its database locks here.
                    assertEquals(1, available(c, 10));
                    borrow(c);
                    if (rollbackA) c.rollback(); else c.commit();
                } finally {
                    c.rollback();
                }
            }
            return null;
        });
        try {
            await(aLocked);
            Future<Integer> b = workers.submit(() -> {
                try (Connection c = connect()) {
                    c.setAutoCommit(false);
                    bPid.complete(backendPid(c));
                    try {
                        lock(c, 10, 20); // Blocks inside PostgreSQL until A finishes.
                        int remaining = available(c, 10); // Separate statement, fresh READ COMMITTED snapshot.
                        c.commit();
                        return remaining;
                    } finally {
                        c.rollback();
                    }
                }
            });
            awaitDatabaseBlocking(bPid.get(5, TimeUnit.SECONDS), aPid.get(5, TimeUnit.SECONDS));
            releaseA.countDown();
            a.get(12, TimeUnit.SECONDS); // Propagate worker exceptions into the test result.
            assertEquals(rollbackA ? 1 : 0, b.get(12, TimeUnit.SECONDS).intValue());
            try (Connection c = connect(); Statement s = c.createStatement();
                 ResultSet r = s.executeQuery("SELECT COALESCE(SUM(quantity), 0) FROM device_borrow_detail")) {
                r.next();
                assertEquals(rollbackA ? 0 : 1, r.getInt(1));
            }
        } finally {
            releaseA.countDown(); // A must not remain stuck if an assertion fails.
        }
    }

    @Test
    void unrelatedDeviceCanBeLockedWhileFirstTransactionStillHoldsItsLock() throws Exception {
        try (Connection a = connect()) {
            a.setAutoCommit(false);
            try {
                lock(a, 10);
                Future<Integer> b = workers.submit(() -> {
                    try (Connection c = connect()) {
                        c.setAutoCommit(false);
                        try {
                            lock(c, 20);
                            int remaining = available(c, 20);
                            c.commit();
                            return remaining;
                        } finally {
                            c.rollback();
                        }
                    }
                });
                assertEquals(1, b.get(5, TimeUnit.SECONDS).intValue());
            } finally {
                a.rollback();
            }
        }
    }
}
