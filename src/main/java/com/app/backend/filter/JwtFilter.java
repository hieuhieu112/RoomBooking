package com.app.backend.filter;


import com.app.backend.entity.User;
import com.app.backend.service.JwtService;
import com.app.backend.utils.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException
    {
        try{
            HttpServletRequest req = (HttpServletRequest) request;
            String path = request.getServletPath();
            if (path.contains("/authen/login") || path.contains("/authen/refresh")) {
                filterChain.doFilter(request, response);
                return;
            }
            log.info("<= {} {} {}ms [{}]", req.getMethod(), req.getRequestURI(), Instant.now(), UUID.randomUUID().toString());

            final String authHeader = request.getHeader("Authorization");
            if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")){
                final String token = authHeader.substring(7);
                // get claim from token
                Claims claims = jwtService.parseClaim(token, Constant.TYPE_ACCESS_TOKEN);
                final String username = jwtService.extractUsernameByClaim(claims);
                final Integer userId = jwtService.extractUserIdByClaims(claims);

                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    List<String> roles = jwtService.extractRolesByClaim(claims);
                    List<GrantedAuthority> authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList());
                    log.info("Authenticating user={} with roles={}", username, roles);

                    if (jwtService.validTokenExpired(token)){
                        User user =User.builder()
                                .username(username)
                                .build();
                        user.setId(userId);
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(user, null, authorities);
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }

            filterChain.doFilter(request, response);
        }
        catch (ExpiredJwtException e) {
            log.error("Token expired: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return;
        } catch (JwtException e) {
            log.error("Invalid token: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return;
        } catch (Exception e) {
            log.error("Unauthorized access: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return;
        } finally {
            MDC.clear();
        }

    }
}
