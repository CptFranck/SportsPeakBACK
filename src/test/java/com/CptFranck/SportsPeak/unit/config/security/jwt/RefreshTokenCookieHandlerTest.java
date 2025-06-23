package com.CptFranck.SportsPeak.unit.config.security.jwt;

import com.CptFranck.SportsPeak.config.security.jwt.RefreshTokenCookieHandler;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

public class RefreshTokenCookieHandlerTest {

    private RefreshTokenCookieHandler handler;

    private MockHttpServletResponse mockResponse;

    @BeforeEach
    void setUp() {
        handler = new RefreshTokenCookieHandler();
        mockResponse = new MockHttpServletResponse();

        ServletRequestAttributes attributes = new ServletRequestAttributes(new MockHttpServletRequest(), mockResponse);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @AfterEach
    void clearContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void addRefreshTokenToCookie_shouldAddCookieWithTokenAndExpiration() {
        String token = "dummy-token";
        handler.addRefreshTokenToCookie(token);

        Cookie cookie = getRefreshTokenCookie();
        Assertions.assertNotNull(cookie);
        Assertions.assertEquals("refreshToken", cookie.getName());
        Assertions.assertEquals(token, cookie.getValue());
        Assertions.assertEquals("/", cookie.getPath());
        Assertions.assertEquals(7 * 24 * 60 * 60, cookie.getMaxAge()); // 7 jours
        Assertions.assertTrue(cookie.isHttpOnly());
        Assertions.assertFalse(cookie.getSecure());
    }

    @Test
    void clearRefreshTokenCookie_shouldClearCookie() {
        handler.clearRefreshTokenCookie();

        Cookie cookie = getRefreshTokenCookie();
        Assertions.assertNotNull(cookie);
        Assertions.assertEquals("refreshToken", cookie.getName());
        Assertions.assertNull(cookie.getValue());
        Assertions.assertEquals(0, cookie.getMaxAge());
    }

    private Cookie getRefreshTokenCookie() {
        return Arrays.stream(mockResponse.getCookies())
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .orElse(null);
    }
}
