package com.CptFranck.SportsPeak.config.security;

import org.springframework.beans.factory.annotation.Value;

public class SecurityProperties {
    public static final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000; // 15 minutes
    public static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 jours
    @Value("${security.jwt.token.secret-key}")
    public static String SECRET_KEY;
}
