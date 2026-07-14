package com.app.backend.config;


import com.app.backend.entity.Role;
import com.app.backend.entity.User;
import com.app.backend.entity.enumm.Status;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.app.backend.repository.RoleRepository;
import com.app.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class InitSystem implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String roleAdmin = "SYSTEM_ADMIN";
        boolean existsRole = roleRepository.existsByName(roleAdmin);
        Role role = new Role();
        if (!existsRole) {

            role.setName(roleAdmin);

            role = roleRepository.save(role);
        }else {
            role = roleRepository.findByName(roleAdmin).orElseThrow(() -> new CommonException(ErrorCode.ROLE_NOT_FOUND));
        }

        String defaultUsername = "adminhub-1";

        // Kiểm tra user đã tồn tại chưa
        boolean exists = userRepository.existsByUsername(defaultUsername);
        if (!exists) {
            User user = new User();
            user.setUsername(defaultUsername);
            user.setPassword(passwordEncoder.encode("admin123")); // mật khẩu mặc định
            user.setName("Quản trị viên");
            user.setEmail("lvtd2311@gmail.com");
            user.setStatus(Status.ACTIVE);
            user.setCreatedBy(0);
            user.setModifyBy(0);
            user.setCreatedAt(LocalDateTime.now());
            user.setModifyAt(LocalDateTime.now());
            user.setRoles(Collections.singleton(role)); // set role admin

            userRepository.save(user);

        } else {
            System.out.println("Default admin user already exists.");
        }
        defaultUsername = "adminhub-2";

        // Kiểm tra user đã tồn tại chưa
        exists = userRepository.existsByUsername(defaultUsername);
        if (!exists) {
            User user = new User();
            user.setUsername(defaultUsername);
            user.setPassword(passwordEncoder.encode("admin123")); // mật khẩu mặc định
            user.setName("Quản trị viên");
            user.setEmail("hieuahieua@gmail.com");
            user.setStatus(Status.ACTIVE);
            user.setCreatedBy(0);
            user.setModifyBy(0);
            user.setCreatedAt(LocalDateTime.now());
            user.setModifyAt(LocalDateTime.now());
            user.setRoles(Collections.singleton(role)); // set role admin

            userRepository.save(user);

        } else {
            System.out.println("Default admin user already exists.");
        }
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
