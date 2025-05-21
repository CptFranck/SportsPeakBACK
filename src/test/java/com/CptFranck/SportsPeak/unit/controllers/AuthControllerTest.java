package com.CptFranck.SportsPeak.unit.controllers;

import com.CptFranck.SportsPeak.controller.AuthController;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.credentials.RegisterInput;
import com.CptFranck.SportsPeak.domain.model.UserToken;
import com.CptFranck.SportsPeak.mapper.Mapper;
import com.CptFranck.SportsPeak.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.CptFranck.SportsPeak.utils.AuthTestUtils.createInputCredentials;
import static com.CptFranck.SportsPeak.utils.AuthTestUtils.createRegisterInput;
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
    private Mapper<UserEntity, UserDto> userMapper;

    private UserEntity user;
    private UserDto userDto;
    private UserToken userToken;
    private AuthDto authDto;

    @BeforeEach
    void setUp() {
        user = createTestUser(1L);
        userDto = createTestUserDto(1L);
        userToken = new UserToken("token", user);
        authDto = new AuthDto("token", userDto);
    }
    @Test
    public void login_ValidCredentials_ReturnsAuthDto() {
        when(authService.login(Mockito.any(InputCredentials.class))).thenReturn(userToken);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        AuthDto authDtoReturn = authController.login(createInputCredentials(user));

        assertEqualsAuth(authDto, authDtoReturn);
    }

    @Test
    public void register_ValidRegisterInput_ReturnsAuthDto() {
        when(authService.register(Mockito.any(RegisterInput.class))).thenReturn(userToken);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        AuthDto authDtoReturn = authController.register(createRegisterInput(user));

        assertEqualsAuth(authDto, authDtoReturn);
    }

    private void assertEqualsAuth(AuthDto authDto, AuthDto authDtoReturn) {
        Assertions.assertEquals(authDto.getUser(), authDtoReturn.getUser());
        Assertions.assertEquals(authDto.getToken(), authDtoReturn.getToken());
    }
}
