package com.CptFranck.SportsPeak.unit.services.services;

import com.CptFranck.SportsPeak.config.security.JwtUtils;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.userAuth.IncorrectPasswordException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.InvalidCredentialsException;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.credentials.RegisterInput;
import com.CptFranck.SportsPeak.domain.input.user.InputUserEmail;
import com.CptFranck.SportsPeak.domain.input.user.InputUserPassword;
import com.CptFranck.SportsPeak.domain.model.CustomUserDetails;
import com.CptFranck.SportsPeak.domain.model.UserToken;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.UserService;
import com.CptFranck.SportsPeak.service.serviceImpl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
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

import static com.CptFranck.SportsPeak.utils.AuthTestUtils.createInputCredentials;
import static com.CptFranck.SportsPeak.utils.AuthTestUtils.createRegisterInput;
import static com.CptFranck.SportsPeak.utils.RoleTestUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.*;
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
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private JwtUtils jwtUtils;

    private String token;
    private RoleEntity role;
    private UserEntity user;
    private UserToken userToken;
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        token = "token";
        role = createTestRole(1L, 0);
        user = createTestUser(1L);
        userToken = new UserToken(token, user);
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
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(userDetailsService.loadUserByUsername(Mockito.any(String.class))).thenReturn(userDetails);
        when(jwtUtils.generateToken(Mockito.any(UserDetails.class))).thenReturn(token);
        when(userService.findByLogin(Mockito.any(String.class))).thenReturn(user);

        InputCredentials inputCredentials = createInputCredentials(user);
        UserToken returnedUser = authServiceImpl.login(inputCredentials);

        Assertions.assertEquals(userToken.getToken(), returnedUser.getToken());
        Assertions.assertEquals(userToken.getUser(), returnedUser.getUser());
    }

    @Test
    void register_ValidRegisterInput_ReturnUserEntity() {
        when(roleService.findByName(Mockito.any(String.class))).thenReturn(role);
        when(passwordEncoder.encode(Mockito.any(String.class))).thenReturn(user.getPassword());
        when(userService.save(Mockito.any(UserEntity.class))).thenReturn(user);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(userDetailsService.loadUserByUsername(Mockito.any(String.class))).thenReturn(userDetails);
        when(jwtUtils.generateToken(Mockito.any(UserDetails.class))).thenReturn(token);

        RegisterInput registerInput = createRegisterInput(user);
        UserToken returnedUser = authServiceImpl.register(registerInput);

        Assertions.assertEquals(userToken.getToken(), returnedUser.getToken());
        Assertions.assertEquals(userToken.getUser(), returnedUser.getUser());
    }

    @Test
    void updateEmail_InvalidPassword_ThrowIncorrectPasswordException() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(user);
        when(passwordEncoder.matches(Mockito.any(CharSequence.class), Mockito.any(String.class))).thenThrow(IncorrectPasswordException.class);

        InputUserEmail inputUserEmail = createTestInputUserEmail(user.getId(), user.getPassword());
        assertThrows(IncorrectPasswordException.class, () -> authServiceImpl.updateEmail(inputUserEmail));
    }

    @Test
    void updateEmail_ValidInputs_ReturnUserEntity() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(user);
        when(passwordEncoder.matches(Mockito.any(CharSequence.class), Mockito.any(String.class))).thenReturn(true);
        when(userService.save(Mockito.any(UserEntity.class))).thenReturn(user);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(userDetailsService.loadUserByUsername(Mockito.any(String.class))).thenReturn(userDetails);
        when(jwtUtils.generateToken(Mockito.any(UserDetails.class))).thenReturn(token);

        InputUserEmail inputUserEmail = createTestInputUserEmail(user.getId(), user.getPassword());
        UserToken returnedUser = authServiceImpl.updateEmail(inputUserEmail);

        Assertions.assertEquals(userToken.getToken(), returnedUser.getToken());
        Assertions.assertEquals(userToken.getUser(), returnedUser.getUser());
    }

    @Test
    void updatePassword_InvalidPassword_ThrowIncorrectPasswordException() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(user);
        when(passwordEncoder.matches(Mockito.any(CharSequence.class), Mockito.any(String.class))).thenReturn(false);

        InputUserPassword inputUserPassword = createTestInputUserPassword(user.getId(), user.getPassword());
        assertThrows(IncorrectPasswordException.class, () -> authServiceImpl.updatePassword(inputUserPassword));
    }

    @Test
    void updatePassword_ValidInputs_ReturnUserEntity() {
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(user);
        when(passwordEncoder.matches(Mockito.any(CharSequence.class), Mockito.any(String.class))).thenReturn(true);
        when(userService.save(Mockito.any(UserEntity.class))).thenReturn(user);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(userDetailsService.loadUserByUsername(Mockito.any(String.class))).thenReturn(userDetails);
        when(jwtUtils.generateToken(Mockito.any(UserDetails.class))).thenReturn(token);

        InputUserPassword inputUserPassword = createTestInputUserPassword(user.getId(), user.getPassword());
        UserToken returnedUser = authServiceImpl.updatePassword(inputUserPassword);

        Assertions.assertEquals(userToken.getToken(), returnedUser.getToken());
        Assertions.assertEquals(userToken.getUser(), returnedUser.getUser());
    }
}
