package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.security.jwt.RefreshTokenCookieHandler;
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
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.security.core.context.SecurityContextHolder;

@DgsComponent
public class AuthController {

    private final AuthService authService;

    private final TokenService tokenService;

    private final Mapper<UserEntity, UserDto> userMapper;

    private final RefreshTokenCookieHandler refreshTokenCookieHandler;

    public AuthController(AuthService authService,
                          TokenService tokenService,
                          Mapper<UserEntity, UserDto> userMapper,
                          RefreshTokenCookieHandler refreshTokenCookieHandler) {
        this.userMapper = userMapper;
        this.authService = authService;
        this.tokenService = tokenService;
        this.refreshTokenCookieHandler = refreshTokenCookieHandler;
    }

    @DgsMutation
    public AuthDto login(@InputArgument InputCredentials inputCredentials) {
        UserTokens userToken = authService.login(inputCredentials);
        refreshTokenCookieHandler.addRefreshTokenToCookie(userToken.getRefreshToken());
        return new AuthDto(userToken.getAccessToken(), userMapper.mapTo(userToken.getUser()));
    }

    @DgsMutation
    public AuthDto register(@InputArgument RegisterInput registerInput) {
        UserTokens userToken = authService.register(registerInput);
        refreshTokenCookieHandler.addRefreshTokenToCookie(userToken.getRefreshToken());
        return new AuthDto(userToken.getAccessToken(), userMapper.mapTo(userToken.getUser()));
    }

    @DgsMutation
    public AuthDto refreshToken(DataFetchingEnvironment env) {
        GraphQLContext context = env.getGraphQlContext();
        String refreshToken = context.get("refreshToken");
        if (refreshToken == null) throw new TokenMissingException("Refresh token missing");

        UserTokens userToken = authService.refreshAccessToken(refreshToken);
        refreshTokenCookieHandler.addRefreshTokenToCookie(userToken.getRefreshToken());

        return new AuthDto(userToken.getAccessToken(), userMapper.mapTo(userToken.getUser()));
    }

    @DgsMutation
    public boolean logout(DataFetchingEnvironment env) {
        GraphQLContext context = env.getGraphQlContext();

        String accessToken = context.get("accessToken");
        String refreshToken = context.get("refreshToken");

        SecurityContextHolder.clearContext();
        refreshTokenCookieHandler.clearRefreshTokenCookie();

        if (accessToken == null || refreshToken == null)
            throw new TokenMissingException("Access and / or refresh missing");

        tokenService.revokeToken(accessToken);
        tokenService.revokeToken(refreshToken);

        return true;
    }
}
