package com.CptFranck.SportsPeak.unit.services.services;

import com.CptFranck.SportsPeak.config.security.jwt.JwtUtils;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.token.InvalidTokenException;
import com.CptFranck.SportsPeak.domain.exception.token.RefreshTokenExpiredException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.InvalidCredentialsException;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.credentials.RegisterInput;
import com.CptFranck.SportsPeak.domain.input.user.InputUserEmail;
import com.CptFranck.SportsPeak.domain.input.user.InputUserPassword;
import com.CptFranck.SportsPeak.domain.model.CustomUserDetails;
import com.CptFranck.SportsPeak.domain.model.UserTokens;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.TokenService;
import com.CptFranck.SportsPeak.service.UserService;
import com.CptFranck.SportsPeak.service.serviceImpl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Date;

import static com.CptFranck.SportsPeak.utils.AuthTestUtils.createInputCredentials;
import static com.CptFranck.SportsPeak.utils.AuthTestUtils.createRegisterInput;
import static com.CptFranck.SportsPeak.utils.RoleTestUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.TokenTestUtils.assertUserTokens;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private JwtUtils jwtUtils;

    private String accessToken;
    private String refreshToken;
    private RoleEntity role;
    private UserEntity user;
    private UserTokens userTokens;
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        accessToken = "accessToken";
        refreshToken = "refreshToken";
        role = createTestRole(1L, 0);
        user = createTestUser(1L);
        userTokens = new UserTokens(user, accessToken, refreshToken);
        userDetails = new CustomUserDetails(user);
    }

    @Test
    void login_authentificationFailed_ThrowInvalidCredentialsException() {
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        InputCredentials inputCredentials = createInputCredentials(user);
        assertThrows(InvalidCredentialsException.class, () -> authServiceImpl.login(inputCredentials));
    }

    @Test
    void login_CorrectCredentials_ReturnUserTokenEntity() {
        when(userDetailsService.loadUserByUsername(Mockito.any(String.class))).thenReturn(userDetails);
        when(userService.findByLogin(Mockito.any(String.class))).thenReturn(user);
        when(jwtUtils.generateAccessToken(Mockito.any(UserDetails.class))).thenReturn(accessToken);
        when(jwtUtils.generateRefreshToken(Mockito.any(UserDetails.class))).thenReturn(refreshToken);
        when(jwtUtils.extractExpiration(Mockito.any(String.class))).thenReturn(Date.from(Instant.now()));

        InputCredentials inputCredentials = createInputCredentials(user);
        UserTokens returnedUser = authServiceImpl.login(inputCredentials);

        assertUserTokens(userTokens, returnedUser);
    }

    @Test
    void register_ValidRegisterInput_ReturnUserEntity() {
        when(roleService.findByName(Mockito.any(String.class))).thenReturn(role);
        when(passwordEncoder.encode(Mockito.any(String.class))).thenReturn(user.getPassword());
        when(userService.save(Mockito.any(UserEntity.class))).thenReturn(user);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(userDetailsService.loadUserByUsername(Mockito.any(String.class))).thenReturn(userDetails);
        when(jwtUtils.generateAccessToken(Mockito.any(UserDetails.class))).thenReturn(accessToken);
        when(jwtUtils.generateRefreshToken(Mockito.any(UserDetails.class))).thenReturn(refreshToken);
        when(jwtUtils.extractExpiration(Mockito.any(String.class))).thenReturn(Date.from(Instant.now()));

        RegisterInput registerInput = createRegisterInput(user);
        UserTokens returnedUser = authServiceImpl.register(registerInput);

        assertUserTokens(userTokens, returnedUser);
    }

    @Test
    void refreshAccessToken_InvalidRefreshToken_ThrowInvalidTokenException() {
        when(jwtUtils.validateToken(Mockito.any(String.class))).thenReturn(false);

        assertThrows(InvalidTokenException.class, () -> authServiceImpl.refreshAccessToken("refreshToken"));
    }

    @Test
    void refreshAccessToken_InvalidRefreshTokenExpired_ThrowRefreshTokenExpiredException() {
        when(jwtUtils.validateToken(Mockito.any(String.class))).thenReturn(true);
        when(tokenService.isTokenValidInStore(Mockito.any(String.class))).thenReturn(false);

        assertThrows(RefreshTokenExpiredException.class, () -> authServiceImpl.refreshAccessToken("refreshToken"));
    }

    @Test
    void refreshAccessToken_InvalidUsernameToken_ThrowInvalidTokenException() {
        when(jwtUtils.validateToken(Mockito.any(String.class))).thenReturn(true);
        when(tokenService.isTokenValidInStore(Mockito.any(String.class))).thenReturn(true);
        when(jwtUtils.extractUsername(Mockito.any(String.class))).thenReturn(null);

        assertThrows(InvalidTokenException.class, () -> authServiceImpl.refreshAccessToken("refreshToken"));
    }

    @Test
    void refreshAccessToken_ValidInputs_ThrowIncorrectPasswordException() {
        when(jwtUtils.validateToken(Mockito.any(String.class))).thenReturn(true);
        when(tokenService.isTokenValidInStore(Mockito.any(String.class))).thenReturn(true);
        when(jwtUtils.extractUsername(Mockito.any(String.class))).thenReturn("username");
        when(userService.findByLogin(Mockito.any(String.class))).thenReturn(user);
        when(userDetailsService.loadUserByUsername(Mockito.any(String.class))).thenReturn(userDetails);
        when(jwtUtils.generateAccessToken(Mockito.any(UserDetails.class))).thenReturn(accessToken);
        when(jwtUtils.generateRefreshToken(Mockito.any(UserDetails.class))).thenReturn(refreshToken);
        when(jwtUtils.extractExpiration(Mockito.any(String.class))).thenReturn(Date.from(Instant.now()));

        UserTokens returnedUser = authServiceImpl.refreshAccessToken("refreshToken");

        assertUserTokens(userTokens, returnedUser);
    }

    @Test
    void updateEmail_InvalidPassword_ThrowIncorrectPasswordException() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(user);
        when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenThrow(new BadCredentialsException("Bad credentials"));

        InputUserEmail inputUserEmail = createTestInputUserEmail(user.getId(), "user.getPassword()");
        assertThrows(InvalidCredentialsException.class, () -> authServiceImpl.updateEmail(inputUserEmail));
    }

    @Test
    void updateEmail_ValidInputs_ReturnUserEntity() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(user);
        when(userService.save(Mockito.any(UserEntity.class))).thenReturn(user);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(userDetailsService.loadUserByUsername(Mockito.any(String.class))).thenReturn(userDetails);
        when(jwtUtils.generateAccessToken(Mockito.any(UserDetails.class))).thenReturn(accessToken);
        when(jwtUtils.generateRefreshToken(Mockito.any(UserDetails.class))).thenReturn(refreshToken);
        when(jwtUtils.extractExpiration(Mockito.any(String.class))).thenReturn(Date.from(Instant.now()));

        InputUserEmail inputUserEmail = createTestInputUserEmail(user.getId(), user.getPassword());
        UserTokens returnedUser = authServiceImpl.updateEmail(inputUserEmail);

        assertUserTokens(userTokens, returnedUser);
    }

    @Test
    void updatePassword_InvalidPassword_ThrowIncorrectPasswordException() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(user);
        when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenThrow(new BadCredentialsException("Bad credentials"));

        InputUserPassword inputUserPassword = createTestInputUserPassword(user.getId(), "user.getPassword()");
        assertThrows(InvalidCredentialsException.class, () -> authServiceImpl.updatePassword(inputUserPassword));
    }

    @Test
    void updatePassword_ValidInputs_ReturnUserEntity() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(user);
        when(userService.save(Mockito.any(UserEntity.class))).thenReturn(user);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(userDetailsService.loadUserByUsername(Mockito.any(String.class))).thenReturn(userDetails);
        when(jwtUtils.generateAccessToken(Mockito.any(UserDetails.class))).thenReturn(accessToken);
        when(jwtUtils.generateRefreshToken(Mockito.any(UserDetails.class))).thenReturn(refreshToken);
        when(jwtUtils.extractExpiration(Mockito.any(String.class))).thenReturn(Date.from(Instant.now()));

        InputUserPassword inputUserPassword = createTestInputUserPassword(user.getId(), user.getPassword());
        UserTokens returnedUser = authServiceImpl.updatePassword(inputUserPassword);

        assertUserTokens(userTokens, returnedUser);
    }
}
