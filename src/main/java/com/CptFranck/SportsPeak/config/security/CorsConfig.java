package com.CptFranck.SportsPeak.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    private static final List<String> HEADERS_ACCEPTED = List.of(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With"
    );
    private static final List<String> METHODS_REST = List.of("GET", "POST");
    private static final List<String> METHODS_GRAPHQL = List.of("POST", "OPTIONS");
    @Value("${security.cors.allowed-origin}")
    private String allowedOrigin;

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration restApiCors = new CorsConfiguration();
        restApiCors.setAllowedOrigins(List.of(allowedOrigin));
        restApiCors.setAllowedMethods(METHODS_REST);
        restApiCors.setAllowedHeaders(HEADERS_ACCEPTED);
        restApiCors.setAllowCredentials(true);

        CorsConfiguration graphqlApiCors = new CorsConfiguration();
        graphqlApiCors.setAllowedOrigins(List.of(allowedOrigin));
        graphqlApiCors.setAllowedMethods(METHODS_GRAPHQL);
        graphqlApiCors.setAllowedHeaders(HEADERS_ACCEPTED);
        graphqlApiCors.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/service/api/graphql/**", graphqlApiCors);
        source.registerCorsConfiguration("/service/api/rest/**", restApiCors);

        return source;
    }
}
