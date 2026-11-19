package com.app.backend.service.impl;

import com.app.backend.entity.User;
import com.app.backend.entity.enumm.Status;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.app.backend.repository.RoleRepository;
import com.app.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminUserService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder passwordEncoder;

    public void resetPassword(Integer userId, String newPass) {
        if (newPass == null || newPass.isBlank() || newPass.length() < 6
                || newPass.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new CommonException(ErrorCode.USER_PASSWORD_INVALID);
        }
        User user = findUser(userId);
        user.setPassword(passwordEncoder.encode(newPass));
        revokeSessions(user);
    }

    public void changeRoles(Integer userId, Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()
                || roleNames.stream().anyMatch(name -> name == null || name.isBlank())) {
            throw new CommonException(ErrorCode.REQUEST_INVALID);
        }
        User user = findUser(userId);
        // Resolve all roles before changing the managed entity; use current DB data.
        var newRoles = roleNames.stream()
                .map(name -> roles.findByName(name)
                        .orElseThrow(() -> new CommonException(ErrorCode.ROLE_NOT_FOUND)))
                .collect(Collectors.toSet());
        user.setRoles(newRoles);
        revokeSessions(user);
    }

    public void setLocked(Integer userId, boolean locked) {
        User user = findUser(userId);
        // This endpoint only switches ACTIVE <-> LOCKED, not disabled/inactive accounts.
        if (user.getStatus() != Status.ACTIVE && user.getStatus() != Status.LOCKED) {
            throw new CommonException(ErrorCode.USER_INVALID_STATUS);
        }
        Status nextStatus = locked ? Status.LOCKED : Status.ACTIVE;
        if (user.getStatus() != nextStatus) {
            user.setStatus(nextStatus);
            revokeSessions(user);
        }
    }

    private User findUser(Integer userId) {
        return users.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
    }

    private void revokeSessions(User user) {
        // Persist the change and revocation together. Old Redis keys expire naturally;
        // JwtFilter and refresh reject their old version immediately after commit.
        user.setAuthVersion(Math.addExact(user.getAuthVersion() == null ? 0 : user.getAuthVersion(), 1));
        users.save(user);
    }
}
