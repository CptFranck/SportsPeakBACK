package com.CptFranck.SportsPeak.unit.services.services;

import com.CptFranck.SportsPeak.config.security.TokenHashConfig;
import com.CptFranck.SportsPeak.domain.entity.TokenEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.enumType.TokenType;
import com.CptFranck.SportsPeak.domain.exception.token.TokenNotFoundException;
import com.CptFranck.SportsPeak.repository.TokenRepository;
import com.CptFranck.SportsPeak.service.serviceImpl.TokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.CptFranck.SportsPeak.utils.TokenTestUtils.assertTokenEntity;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenServiceImplTest {

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private TokenHashConfig.Sha256Hasher sha256Hasher;

    @Mock
    private TokenRepository tokenRepository;

    @Test
    void hashToken_ValidInput_ReturnHashString() {
        when(sha256Hasher.hash(Mockito.any(String.class))).thenReturn("hashedToken");

        String hashedToken = tokenService.hashToken("token");

        assertEquals("hashedToken", hashedToken);
    }

    @Test
    void save_ValidTokenInput_ReturnTokenEntity() {
        UserEntity user = createTestUser(1L);
        TokenEntity token = new TokenEntity("Token", TokenType.ACCESS, Instant.now(), user);
        when(tokenService.save(Mockito.any(TokenEntity.class))).thenReturn(token);

        TokenEntity tokenSaved = tokenService.save(token);

        assertTokenEntity(token, tokenSaved, true);
    }

    @Test
    void isTokenValidInStore_NoTokenFound_ReturnFalse() {
        when(sha256Hasher.hash(Mockito.any(String.class))).thenReturn("hashedToken");
        when(tokenRepository.findByToken(Mockito.any(String.class))).thenReturn(Optional.empty());

        boolean isValidToken = tokenService.isTokenValidInStore("token");

        assertFalse(isValidToken);
    }

    @Test
    void isTokenValidInStore_InValidToken_ReturnFalse() {
        UserEntity user = createTestUser(1L);
        TokenEntity token = new TokenEntity("Token", TokenType.ACCESS, Instant.now(), user);
        token.setExpired(true);
        token.setRevoked(true);
        when(sha256Hasher.hash(Mockito.any(String.class))).thenReturn("hashedToken");
        when(tokenRepository.findByToken(Mockito.any(String.class))).thenReturn(Optional.of(token));

        boolean isValidToken = tokenService.isTokenValidInStore("token");

        assertFalse(isValidToken);
    }

    @Test
    void isTokenValidInStore_ValidToken_ReturnTrue() {
        UserEntity user = createTestUser(1L);
        TokenEntity token = new TokenEntity("Token", TokenType.ACCESS, Instant.now(), user);
        when(sha256Hasher.hash(Mockito.any(String.class))).thenReturn("hashedToken");
        when(tokenRepository.findByToken(Mockito.any(String.class))).thenReturn(Optional.of(token));

        boolean isValidToken = tokenService.isTokenValidInStore("token");

        assertTrue(isValidToken);
    }

    @Test
    void revokeToken_NoTokenFound_ThrowTokenNotFoundException() {
        UserEntity user = createTestUser(1L);
        TokenEntity token = new TokenEntity("Token", TokenType.ACCESS, Instant.now(), user);
        when(sha256Hasher.hash(Mockito.any(String.class))).thenReturn("hashedToken");
        when(tokenRepository.findByToken(Mockito.any(String.class))).thenReturn(Optional.empty());

        assertThrows(TokenNotFoundException.class, () -> tokenService.revokeToken("token"));
    }

    @Test
    void revokeToken_ValidToken_Void() {
        UserEntity user = createTestUser(1L);
        TokenEntity token = new TokenEntity("Token", TokenType.ACCESS, Instant.now(), user);
        when(sha256Hasher.hash(Mockito.any(String.class))).thenReturn("hashedToken");
        when(tokenRepository.findByToken(Mockito.any(String.class))).thenReturn(Optional.of(token));

        assertAll(() -> tokenService.isTokenValidInStore("token"));
    }

    @Test
    void revokeAllUserTokens_ValidUserTokens_Void() {
        UserEntity user = createTestUser(1L);
        TokenEntity token = new TokenEntity("Token", TokenType.ACCESS, Instant.now(), user);
        when(tokenRepository.findAllValidTokenByUser(Mockito.any(Long.class))).thenReturn(List.of(token));

        assertAll(() -> tokenService.revokeAllUserTokens(user));
    }
}
