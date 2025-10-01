package com.CptFranck.SportsPeak.unit.controllers;

import com.CptFranck.SportsPeak.config.security.jwt.RefreshTokenCookieHandler;
import com.CptFranck.SportsPeak.controller.AuthController;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.token.TokenMissingException;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.credentials.RegisterInput;
import com.CptFranck.SportsPeak.domain.model.UserTokens;
import com.CptFranck.SportsPeak.mapper.Mapper;
import com.CptFranck.SportsPeak.service.AuthService;
import com.CptFranck.SportsPeak.service.TokenService;
import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
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
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUser;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUserDto;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private RefreshTokenCookieHandler refreshTokenCookieHandler;

    @Mock
    private AuthService authService;

    @Mock
    private TokenService tokenService;

    @Mock
    private Mapper<UserEntity, UserDto> userMapper;

    private UserEntity user;
    private UserDto userDto;
    private UserTokens userToken;
    private AuthDto authDto;

    @BeforeEach
    void setUp() {
        user = createTestUser(1L);
        userDto = createTestUserDto(1L);
        userToken = new UserTokens(user, "accessToken", "refreshToken");
        authDto = new AuthDto("accessToken", userDto);
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

    @Test
    public void refreshToken_WithNoTokenRefresh_ThrowsTokenMissingException() {
        DataFetchingEnvironment env = Mockito.mock(DataFetchingEnvironment.class);
        when(env.getGraphQlContext()).thenReturn(Mockito.mock(GraphQLContext.class));

        assertThrows(TokenMissingException.class, () -> authController.refreshToken(env));
    }

    @Test
    public void refreshToken_WithTokenRefresh_ReturnsAuthDto() {
        DataFetchingEnvironment env = Mockito.mock(DataFetchingEnvironment.class);
        GraphQLContext context = Mockito.mock(GraphQLContext.class);
        UserTokens usertokens = new UserTokens(user, "accessToken", "refreshToken");
        when(env.getGraphQlContext()).thenReturn(context);
        when(context.get("refreshToken")).thenReturn("refreshToken");
        when(authService.refreshAccessToken(Mockito.any(String.class))).thenReturn(usertokens);
        when(userMapper.mapTo(Mockito.any(UserEntity.class))).thenReturn(userDto);

        AuthDto authDtoReturn = authController.refreshToken(env);

        assertEqualsAuth(authDto, authDtoReturn);
    }

    @Test
    public void logout_WithNoRefreshToken_ThrowsTokenMissingException() {
        DataFetchingEnvironment env = Mockito.mock(DataFetchingEnvironment.class);
        when(env.getGraphQlContext()).thenReturn(Mockito.mock(GraphQLContext.class));

        assertThrows(TokenMissingException.class, () -> authController.logout(env));
    }

    @Test
    public void logout_WithNoAccessToken_ThrowsTokenMissingException() {
        DataFetchingEnvironment env = Mockito.mock(DataFetchingEnvironment.class);
        GraphQLContext context = Mockito.mock(GraphQLContext.class);
        when(env.getGraphQlContext()).thenReturn(context);
        when(context.get("accessToken")).thenReturn("accessToken");

        assertThrows(TokenMissingException.class, () -> authController.logout(env));
    }

    @Test
    public void logout_ValidRegisterInput_ReturnsAuthDto() {
        DataFetchingEnvironment env = Mockito.mock(DataFetchingEnvironment.class);
        GraphQLContext context = Mockito.mock(GraphQLContext.class);
        when(env.getGraphQlContext()).thenReturn(context);
        when(context.get("accessToken")).thenReturn("accessToken");
        when(context.get("refreshToken")).thenReturn("refreshToken");

        boolean authDtoReturn = authController.logout(env);

        assertTrue(authDtoReturn);
    }

    private void assertEqualsAuth(AuthDto authDto, AuthDto authDtoReturn) {
        Assertions.assertEquals(authDto.getUser(), authDtoReturn.getUser());
        Assertions.assertEquals(authDto.getAccessToken(), authDtoReturn.getAccessToken());
    }
}
