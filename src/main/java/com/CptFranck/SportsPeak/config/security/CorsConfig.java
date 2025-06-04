package com.CptFranck.SportsPeak.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration restApiCors = new CorsConfiguration();
        restApiCors.setAllowedOrigins(List.of("http://localhost:4200"));
        restApiCors.setAllowedMethods(List.of("GET", "POST")); //, "PUT", "DELETE", "OPTIONS"
        restApiCors.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"));
        restApiCors.setAllowCredentials(true);

        CorsConfiguration graphqlApiCors = new CorsConfiguration();
        graphqlApiCors.setAllowedOrigins(List.of("http://localhost:4200"));
        graphqlApiCors.setAllowedMethods(List.of("POST", "OPTIONS"));
        graphqlApiCors.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        graphqlApiCors.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/service/api/graphql/**", graphqlApiCors);
        source.registerCorsConfiguration("/service/api/rest/**", restApiCors);

        return source;
    }
}
