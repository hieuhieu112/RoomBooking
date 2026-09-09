package com.app.backend.provider;

import com.app.backend.config.AppProperties;
import com.app.backend.constant.SecurityConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class SecurityEndpointProvider {
    private final AppProperties appProperties;

    public String[] getPublicEndpoints() {
        String prefix = appProperties.getApiPrefix();

        return Arrays.stream(SecurityConstant.PUBLIC_ENDPOINTS)
                .map(endpoint -> prefix + endpoint)
                .toArray(String[]::new);
    }

}
