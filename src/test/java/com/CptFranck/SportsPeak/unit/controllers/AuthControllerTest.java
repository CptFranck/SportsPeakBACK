package com.CptFranck.SportsPeak.unit.controllers;

import com.CptFranck.SportsPeak.config.security.JwtProvider;
import com.CptFranck.SportsPeak.controller.AuthController;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.user.InputRegisterNewUser;
import com.CptFranck.SportsPeak.domain.model.JWToken;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.LocalDateTime;

import static com.CptFranck.SportsPeak.utils.AuthUtils.createInputCredentials;
import static com.CptFranck.SportsPeak.utils.AuthUtils.createInputRegisterNewUser;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUserDto;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private JwtProvider userAuthProvider;

    @Mock
    private Mapper<UserEntity, UserDto> userMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    private UserEntity user;
    private UserDto userDto;
    private JWToken jwToken;
    private AuthDto authDto;

    @BeforeEach
    void setUp() {
        user = createTestUser(1L);
        userDto = createTestUserDto(1L);
        jwToken = new JWToken("token", LocalDateTime.now());
        authDto = new AuthDto(jwToken, userDto);
    }
    @Test
    public void login_ValidCredentials_ReturnsAuthDto() {
        when(authService.login(Mockito.any(InputCredentials.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userAuthProvider.generateToken(Mockito.any())).thenReturn(jwToken);

        AuthDto authDtoReturn = authController.login(createInputCredentials(user));

        assertEqualsAuth(authDto, authDtoReturn);
    }

    @Test
    public void register_ValidCredentials_ReturnsAuthDto() {
        when(authService.register(Mockito.any(InputRegisterNewUser.class))).thenReturn(user);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userAuthProvider.generateToken(Mockito.any())).thenReturn(jwToken);

        AuthDto authDtoReturn = authController.register(createInputRegisterNewUser(user));

        assertEqualsAuth(authDto, authDtoReturn);
    }

    private void assertEqualsAuth(AuthDto authDto, AuthDto authDtoReturn) {
        Assertions.assertEquals(authDto.getUser(), authDtoReturn.getUser());
        Assertions.assertEquals(authDto.getExpiration(), authDtoReturn.getExpiration());
        Assertions.assertEquals(authDto.getAccessToken(), authDtoReturn.getAccessToken());
    }
}
