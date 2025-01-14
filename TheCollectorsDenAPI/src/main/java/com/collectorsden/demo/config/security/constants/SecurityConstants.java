package com.collectorsden.demo.config.security.constants;

public final class SecurityConstants {
    public static final String[] WHITE_LIST_URLS = {
            "/api/v1/auth/**",
            "/api/swagger-ui/**",
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",
            "/swagger-ui.html"
    };
}

