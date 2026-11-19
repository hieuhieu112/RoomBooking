package com.app.backend.service.impl;

import com.app.backend.entity.Role;
import com.app.backend.entity.User;
import com.app.backend.entity.enumm.Status;
import com.app.backend.exception.CommonException;
import com.app.backend.repository.RoleRepository;
import com.app.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminUserServiceTest {
    private final UserRepository users = mock(UserRepository.class);
    private final RoleRepository roles = mock(RoleRepository.class);
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
    private final AdminUserService service = new AdminUserService(users, roles, encoder);
    private User target;

    @BeforeEach
    void setup() {
        target = new User();
        target.setId(12);
        target.setAuthVersion(7);
        target.setStatus(Status.ACTIVE);
        when(users.findById(12)).thenReturn(Optional.of(target));
    }

    @Test
    void passwordResetHashesPasswordAndInvalidatesAllOldTokenVersions() {
        service.resetPassword(12, "new-password");
        assertTrue(encoder.matches("new-password", target.getPassword()));
        assertEquals(8, target.getAuthVersion());
        verify(users).save(target);
    }

    @Test
    void roleReplacementUsesDatabaseRolesAndRevokesSessions() {
        Role old = new Role();
        old.setName("SYSTEM_ADMIN");
        Role student = new Role();
        student.setName("STUDENT");
        target.setRoles(Set.of(old));
        when(roles.findByName("STUDENT")).thenReturn(Optional.of(student));
        service.changeRoles(12, Set.of("STUDENT"));
        assertEquals(Set.of(student), target.getRoles());
        assertEquals(8, target.getAuthVersion());
    }

    @Test
    void unknownRoleDoesNotChangeRolesOrVersion() {
        Role old = new Role();
        target.setRoles(Set.of(old));
        when(roles.findByName("missing")).thenReturn(Optional.empty());
        assertThrows(CommonException.class, () -> service.changeRoles(12, Set.of("missing")));
        assertEquals(Set.of(old), target.getRoles());
        assertEquals(7, target.getAuthVersion());
        verify(users, never()).save(any());
    }

    @Test
    void lockingAndUnlockingNeverReviveOldTokensAndRepeatedLockIsIdempotent() {
        service.setLocked(12, true);
        assertEquals(Status.LOCKED, target.getStatus());
        assertEquals(8, target.getAuthVersion());
        service.setLocked(12, true);
        assertEquals(8, target.getAuthVersion());
        service.setLocked(12, false);
        assertEquals(Status.ACTIVE, target.getStatus());
        assertEquals(9, target.getAuthVersion());
    }

    @Test
    void lockEndpointCannotActivateDisabledOrInactiveAccounts() {
        for (Status status : new Status[]{Status.DISABLED, Status.INACTIVE}) {
            target.setStatus(status);
            assertThrows(CommonException.class, () -> service.setLocked(12, false));
            assertEquals(status, target.getStatus());
            assertEquals(7, target.getAuthVersion());
        }
    }

    @Test
    void rejectsInvalidInputAndMissingUser() {
        assertThrows(CommonException.class, () -> service.resetPassword(12, "short"));
        assertThrows(CommonException.class, () -> service.resetPassword(12, "é".repeat(37)));
        assertThrows(CommonException.class, () -> service.changeRoles(12, Set.of()));
        assertThrows(CommonException.class, () -> service.resetPassword(99, "new-password"));
        verify(users, never()).save(any());
    }
}
