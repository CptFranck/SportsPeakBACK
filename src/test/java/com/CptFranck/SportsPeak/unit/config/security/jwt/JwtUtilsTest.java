package com.CptFranck.SportsPeak.unit.config.security.jwt;

import com.CptFranck.SportsPeak.config.security.jwt.JwtUtils;
import com.CptFranck.SportsPeak.domain.model.CustomUserDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUser;
import static org.assertj.core.api.Assertions.assertThat;

public class JwtUtilsTest {

    private final long jwtExpiration = 3000;
    private final long refreshTokenExpiration = 10000;
    @InjectMocks
    private JwtUtils jwtUtils;
    @Mock
    private UserDetailsService userDetailsService;
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        userDetails = new CustomUserDetails(createTestUser(1L));
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtUtils, "secretKey", "ThisIsASecretKeyWith32Characters!!");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpiration", jwtExpiration);
        ReflectionTestUtils.setField(jwtUtils, "refreshTokenExpiration", refreshTokenExpiration);
    }

    @Test
    public void generateAccessToken_ValidInput_ReturnAccessToken() {
        String accessToken = jwtUtils.generateAccessToken(userDetails);

        Assertions.assertNotNull(accessToken);
        assertTokenExpirationCloseToExpected(accessToken, jwtExpiration);
    }

    @Test
    public void generateRefreshToken_ValidInput_ReturnRefreshToken() {
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);

        Assertions.assertNotNull(refreshToken);
        assertTokenExpirationCloseToExpected(refreshToken, refreshTokenExpiration);
    }

    @Test
    public void extractUsername_ValidInput_() {
        String token = jwtUtils.generateAccessToken(userDetails);

        String username = jwtUtils.extractUsername(token);

        Assertions.assertEquals(userDetails.getUsername(), username);
    }

    @Test
    public void extractExpiration_ValidInput_() {
        String token = jwtUtils.generateAccessToken(userDetails);
        ;

        Date date = jwtUtils.extractExpiration(token);

        Assertions.assertNotNull(date);
    }

    private void assertTokenExpirationCloseToExpected(String token, long validityMs) {
        Date expected = new Date(System.currentTimeMillis() + validityMs);
        Date actual = jwtUtils.extractExpiration(token);
        assertThat(actual).isCloseTo(expected, 2000);
    }
}
