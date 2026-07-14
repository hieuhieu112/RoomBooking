package com.app.backend.config;


import com.app.backend.filter.JwtFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {
    private final JwtFilter jwtTokenFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> {})
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(HttpMethod.POST,
                                    "/api/v1/authen/login","/api/v1/authen/refresh","/api/v1/authen/logout")
                            .permitAll()
                            .requestMatchers("/files/**").permitAll()
                            .requestMatchers(
                                    "/",
                                    "/firebase-messaging-sw.js",
                                    "/index.html",
                                    "/assets/**",
                                    "/favicon.ico",
                                    "/app/**",
                                    "/avatars/**",
                                    "/auth/login",
                                    "/actuator/health"
                            ).permitAll()
                            .anyRequest().authenticated();
                });

        return http.build();
    }
}
