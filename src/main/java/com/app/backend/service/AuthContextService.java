package com.app.backend.service;

import com.app.backend.dtos.internal.AuthContext;
import com.app.backend.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class AuthContextService {
    public static AuthContext getContext() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null
                    || !auth.isAuthenticated()
                    || "anonymousUser".equals(auth.getPrincipal())) {
                return new AuthContext("anonymous", 0, Set.of());
            }

            Set<String> roles = auth.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .collect(Collectors.toSet());

            Object principal = auth.getPrincipal();

            if (principal instanceof User user) {
                Integer userId = user.getId();
                String username = user.getUsername();
                log.info("user id: {} username: {}", userId, username);
                return new AuthContext(username, userId, roles);
            }
            return new AuthContext(auth.getName(), 0, roles);

        } catch (Exception e) {
            return new AuthContext("anonymous", 0, Set.of());
        }
    }
}
