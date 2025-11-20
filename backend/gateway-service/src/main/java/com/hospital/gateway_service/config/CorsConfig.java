package com.hospital.gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // 1. Allow Credentials
        corsConfig.setAllowCredentials(true);

        // 2. EXPLICITLY CLEAR Allowed Origins to ensure no "*" exists
        // This is the "Paranoid" step to prevent the specific error you are seeing
        corsConfig.setAllowedOrigins(Collections.emptyList());

        // 3. Use Allowed Origin Patterns (The correct way to handle wildcards + credentials)
        corsConfig.addAllowedOriginPattern("*");

        // 4. Allowed Methods
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 5. Allowed Headers
        corsConfig.setAllowedHeaders(Collections.singletonList("*"));

        // 6. Apply to all routes
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}