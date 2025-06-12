package com.CptFranck.SportsPeak.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;

@Component
public class RefreshTokenCookieHandler {
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private static final int REFRESH_TOKEN_EXPIRATION = (int) Duration.ofDays(7).getSeconds();

    public void addRefreshTokenToCookie(String refreshToken) {
        setRefreshTokenCookie(refreshToken, REFRESH_TOKEN_EXPIRATION);
    }

    public void clearRefreshTokenCookie() {
        setRefreshTokenCookie(null, 0);
    }

    private void setRefreshTokenCookie(String refreshToken, int expiration) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletResponse response = attributes.getResponse();
            if (response != null) {
                Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
                refreshTokenCookie.setHttpOnly(true);
                refreshTokenCookie.setSecure(false); // PROD -> True != False -> Dev
                refreshTokenCookie.setPath("/");
                refreshTokenCookie.setMaxAge(expiration);
                response.addCookie(refreshTokenCookie);
            }
        }
    }
}
