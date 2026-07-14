import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class InitProject {
    static String basePkg = "com.app.backend";
    static String baseDir = "src/main/java/com/app/backend/";
    
    public static void main(String[] args) throws Exception {
        Files.createDirectories(Paths.get(baseDir + "entity"));
        Files.createDirectories(Paths.get(baseDir + "dtos/request"));
        Files.createDirectories(Paths.get(baseDir + "dtos/response"));
        Files.createDirectories(Paths.get(baseDir + "repository"));
        Files.createDirectories(Paths.get(baseDir + "service/impl"));
        Files.createDirectories(Paths.get(baseDir + "controller"));
        
        // 1. pom.xml
        String pom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" " +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
            "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
            "    <modelVersion>4.0.0</modelVersion>\n" +
            "    <parent>\n" +
            "        <groupId>org.springframework.boot</groupId>\n" +
            "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
            "        <version>3.2.0</version>\n" +
            "    </parent>\n" +
            "    <groupId>com.app</groupId>\n" +
            "    <artifactId>backend</artifactId>\n" +
            "    <version>0.0.1-SNAPSHOT</version>\n" +
            "    <properties>\n" +
            "        <java.version>17</java.version>\n" +
            "    </properties>\n" +
            "    <dependencies>\n" +
            "        <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-data-jpa</artifactId></dependency>\n" +
            "        <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-web</artifactId></dependency>\n" +
            "        <dependency><groupId>org.postgresql</groupId><artifactId>postgresql</artifactId><scope>runtime</scope></dependency>\n" +
            "        <dependency><groupId>org.projectlombok</groupId><artifactId>lombok</artifactId><optional>true</optional></dependency>\n" +
            "    </dependencies>\n" +
            "</project>";
        Files.writeString(Paths.get("pom.xml"), pom);

        // Application.java
        String appClass = "package com.app.backend;\nimport org.springframework.boot.SpringApplication;\nimport org.springframework.boot.autoconfigure.SpringBootApplication;\n" +
            "@SpringBootApplication\npublic class BackendApplication {\n    public static void main(String[] args) {\n        SpringApplication.run(BackendApplication.class, args);\n    }\n}\n";
        Files.writeString(Paths.get(baseDir + "BackendApplication.java"), appClass);

        // application.yml
        Files.createDirectories(Paths.get("src/main/resources"));
        String yml = "spring:\n  datasource:\n    url: jdbc:postgresql://localhost:5432/appdb\n    username: postgres\n    password: password\n  jpa:\n    hibernate:\n      ddl-auto: update\n    properties:\n      hibernate:\n        dialect: org.hibernate.dialect.PostgreSQLDialect\n";
        Files.writeString(Paths.get("src/main/resources/application.yml"), yml);

        // BaseEntity
        String baseEntity = "package com.app.backend.entity;\nimport jakarta.persistence.*;\nimport lombok.*;\nimport java.time.LocalDateTime;\n" +
            "@MappedSuperclass\n@Getter @Setter\npublic abstract class BaseEntity {\n" +
            "    @Column(name = \"created_at\", nullable = false, updatable = false)\n    private LocalDateTime createdAt = LocalDateTime.now();\n" +
            "    @Column(name = \"created_by\", nullable = false)\n    private Integer createdBy;\n" +
            "    @Column(name = \"modify_at\", nullable = false)\n    private LocalDateTime modifyAt = LocalDateTime.now();\n" +
            "    @Column(name = \"modify_by\", nullable = false)\n    private Integer modifyBy;\n" +
            "    @Version\n    @Column(nullable = false)\n    private Integer version = 1;\n" +
            "    @Column(name = \"is_deleted\", nullable = false)\n    private Boolean isDeleted = false;\n}\n";
        Files.writeString(Paths.get(baseDir + "entity/BaseEntity.java"), baseEntity);

        // Entity Definitions
        String[][] entities = {
            // Name, HasAudit, Fields... (Type Name)
            {"Booking", "true", "String reason", "LocalDateTime startTime", "LocalDateTime endTime", "Integer status", "Integer userId", "Integer roomId", "Integer approvedByUserId"},
            {"Device", "true", "String name", "Integer status", "Integer deviceCategoryId", "Integer deviceModelId", "Integer roomId"},
            {"DeviceBorrowDetail", "false", "Integer bookingId", "Integer deviceId", "String note"},
            {"DeviceCategory", "false", "String name"},
            {"DeviceIncident", "true", "String description", "LocalDateTime time", "Integer status", "Integer userId", "Integer managerId", "Integer bookingId", "Integer lostBy"},
            {"DeviceIncidentDetail", "false", "Integer deviceIncidentId", "Integer deviceId"},
            {"DeviceModel", "false", "String name", "Integer deviceTypeId", "Integer manufacturerDeviceId", "String specifications"},
            {"DeviceType", "false", "String name"},
            {"House", "false", "String name"},
            {"ManagerGroup", "false", "String name"},
            {"ManufacturerDevice", "false", "String name"},
            {"RefreshToken", "false", "LocalDateTime expiryDate", "String token", "Integer userId"},
            {"Role", "false", "String name"},
            {"Room", "true", "String name", "String location", "Integer capacity", "Integer roomTypeId", "Integer houseId", "String image", "Integer managerGroupId"},
            {"RoomImage", "false", "String url", "Integer roomId"},
            {"RoomType", "true", "String name"},
            {"SystemLog", "false", "Integer userId", "Integer action", "Integer targetType", "LocalDateTime timestamp", "Boolean isDeleted", "String description"},
            {"User", "true", "String name", "String email", "Integer incidentCount", "String pass", "Integer status", "String username", "Integer managerGroupId"}
        };

        for (String[] ent : entities) {
            String eName = ent[0];
            boolean hasAudit = "true".equals(ent[1]);
            StringBuilder fields = new StringBuilder();
            StringBuilder dFields = new StringBuilder();
            StringBuilder setEnt = new StringBuilder();
            StringBuilder setDto = new StringBuilder();

            for (int i = 2; i < ent.length; i++) {
                String[] parts = ent[i].split(" ");
                String type = parts[0];
                String name = parts[1];
                fields.append("    private ").append(type).append(" ").append(name).append(";\n");
                dFields.append("    private ").append(type).append(" ").append(name).append(";\n");
                
                String capName = name.substring(0, 1).toUpperCase() + name.substring(1);
                setEnt.append("        entity.set").append(capName).append("(request.get").append(capName).append("());\n");
                setDto.append("        resp.set").append(capName).append("(entity.get").append(capName).append("());\n");
            }

            // Entity Class
            String eClass = "package com.app.backend.entity;\nimport jakarta.persistence.*;\nimport lombok.*;\nimport java.time.LocalDateTime;\n" +
                "@Entity\n@Table(name = \"" + eName.toLowerCase() + "\")\n@Getter @Setter @NoArgsConstructor @AllArgsConstructor\n" +
                "public class " + eName + (hasAudit ? " extends BaseEntity" : "") + " {\n" +
                "    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)\n    private Integer id;\n" + fields + "}\n";
            Files.writeString(Paths.get(baseDir + "entity/" + eName + ".java"), eClass);

            // Create Request DTO
            String cDto = "package com.app.backend.dtos.request;\nimport lombok.*;\nimport java.time.LocalDateTime;\n@Getter @Setter @NoArgsConstructor @AllArgsConstructor\npublic class Create" + eName + "Request {\n" + dFields + "}\n";
            Files.writeString(Paths.get(baseDir + "dtos/request/Create" + eName + "Request.java"), cDto);

            // Update Request DTO
            String uDto = "package com.app.backend.dtos.request;\nimport lombok.*;\nimport java.time.LocalDateTime;\n@Getter @Setter @NoArgsConstructor @AllArgsConstructor\npublic class Update" + eName + "Request {\n" + dFields + "}\n";
            Files.writeString(Paths.get(baseDir + "dtos/request/Update" + eName + "Request.java"), uDto);

            // Response DTO
            String rDto = "package com.app.backend.dtos.response;\nimport lombok.*;\nimport java.time.LocalDateTime;\n@Getter @Setter @NoArgsConstructor @AllArgsConstructor\npublic class " + eName + "Response {\n    private Integer id;\n" + dFields + "}\n";
            Files.writeString(Paths.get(baseDir + "dtos/response/" + eName + "Response.java"), rDto);

            // Repo
            String repo = "package com.app.backend.repository;\nimport com.app.backend.entity." + eName + ";\nimport org.springframework.data.jpa.repository.JpaRepository;\nimport org.springframework.stereotype.Repository;\n@Repository\npublic interface " + eName + "Repository extends JpaRepository<" + eName + ", Integer> {}\n";
            Files.writeString(Paths.get(baseDir + "repository/" + eName + "Repository.java"), repo);

            // Service Interface
            String sInt = "package com.app.backend.service;\nimport com.app.backend.dtos.request.*;\nimport com.app.backend.dtos.response.*;\nimport java.util.List;\npublic interface " + eName + "Service {\n    " + eName + "Response create(Create" + eName + "Request request);\n    " + eName + "Response getById(Integer id);\n    List<" + eName + "Response> getAll();\n    " + eName + "Response update(Integer id, Update" + eName + "Request request);\n    void delete(Integer id);\n}\n";
            Files.writeString(Paths.get(baseDir + "service/" + eName + "Service.java"), sInt);

            // Service Impl
            String lName = eName.substring(0,1).toLowerCase() + eName.substring(1);
            String sImpl = "package com.app.backend.service.impl;\nimport com.app.backend.dtos.request.*;\nimport com.app.backend.dtos.response.*;\nimport com.app.backend.entity." + eName + ";\nimport com.app.backend.repository." + eName + "Repository;\nimport com.app.backend.service." + eName + "Service;\nimport org.springframework.stereotype.Service;\nimport org.springframework.transaction.annotation.Transactional;\nimport java.util.List;\nimport java.util.stream.Collectors;\n\n@Service\n@Transactional\npublic class " + eName + "ServiceImpl implements " + eName + "Service {\n    private final " + eName + "Repository repo;\n    public " + eName + "ServiceImpl(" + eName + "Repository repo) { this.repo = repo; }\n\n    private " + eName + "Response mapToResponse(" + eName + " entity) {\n        " + eName + "Response resp = new " + eName + "Response();\n        resp.setId(entity.getId());\n" + setDto + "        return resp;\n    }\n\n    @Override\n    public " + eName + "Response create(Create" + eName + "Request request) {\n        " + eName + " entity = new " + eName + "();\n" + setEnt + "        entity = repo.save(entity);\n        return mapToResponse(entity);\n    }\n\n    @Override\n    public " + eName + "Response getById(Integer id) {\n        return repo.findById(id).map(this::mapToResponse).orElse(null);\n    }\n\n    @Override\n    public List<" + eName + "Response> getAll() {\n        return repo.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());\n    }\n\n    @Override\n    public " + eName + "Response update(Integer id, Update" + eName + "Request request) {\n        " + eName + " entity = repo.findById(id).orElseThrow(() -> new RuntimeException(\"Not found\"));\n" + setEnt + "        entity = repo.save(entity);\n        return mapToResponse(entity);\n    }\n\n    @Override\n    public void delete(Integer id) { repo.deleteById(id); }\n}\n";
            Files.writeString(Paths.get(baseDir + "service/impl/" + eName + "ServiceImpl.java"), sImpl);

            // Controller
            String ctrl = "package com.app.backend.controller;\nimport com.app.backend.dtos.request.*;\nimport com.app.backend.dtos.response.*;\nimport com.app.backend.service." + eName + "Service;\nimport org.springframework.http.ResponseEntity;\nimport org.springframework.web.bind.annotation.*;\nimport java.util.List;\n\n@RestController\n@RequestMapping(\"/api/v1/" + eName.toLowerCase() + "s\")\npublic class " + eName + "Controller {\n    private final " + eName + "Service service;\n    public " + eName + "Controller(" + eName + "Service service) { this.service = service; }\n\n    @PostMapping\n    public ResponseEntity<" + eName + "Response> create(@RequestBody Create" + eName + "Request request) {\n        return ResponseEntity.ok(service.create(request));\n    }\n\n    @GetMapping(\"/{id}\")\n    public ResponseEntity<" + eName + "Response> getById(@PathVariable Integer id) {\n        " + eName + "Response res = service.getById(id);\n        return res != null ? ResponseEntity.ok(res) : ResponseEntity.notFound().build();\n    }\n\n    @GetMapping\n    public ResponseEntity<List<" + eName + "Response>> getAll() {\n        return ResponseEntity.ok(service.getAll());\n    }\n\n    @PutMapping(\"/{id}\")\n    public ResponseEntity<" + eName + "Response> update(@PathVariable Integer id, @RequestBody Update" + eName + "Request request) {\n        return ResponseEntity.ok(service.update(id, request));\n    }\n\n    @DeleteMapping(\"/{id}\")\n    public ResponseEntity<Void> delete(@PathVariable Integer id) {\n        service.delete(id);\n        return ResponseEntity.noContent().build();\n    }\n}\n";
            Files.writeString(Paths.get(baseDir + "controller/" + eName + "Controller.java"), ctrl);
        }
        System.out.println("Generation completed!");
    }
}
