package com.app.backend.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {
    private Jwt jwt;
    private Cookie cookie;

    @Getter
    @Setter
    public static class Jwt {
        private String accesssecret;
        private String refreshsecret;
        private Long accessTokenExpireSeconds;
        private Long refreshTokenExpireSeconds;
    }

    @Getter @Setter
    public static class Cookie {
        private boolean secure;
        private String sameSite;
    }
}
