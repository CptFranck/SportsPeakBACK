package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.credentials.RegisterInput;
import com.CptFranck.SportsPeak.domain.model.UserToken;
import com.CptFranck.SportsPeak.mapper.Mapper;
import com.CptFranck.SportsPeak.service.AuthService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;

@DgsComponent
public class AuthController {

    private final AuthService authService;

    private final Mapper<UserEntity, UserDto> userMapper;

    public AuthController(AuthService authService, Mapper<UserEntity, UserDto> userMapper) {
        this.userMapper = userMapper;
        this.authService = authService;
    }

    @DgsMutation
    public AuthDto login(@InputArgument InputCredentials inputCredentials) {
        UserToken userToken = authService.login(inputCredentials);
        return new AuthDto(userToken.getToken(), userMapper.mapTo(userToken.getUser()));
    }

    @DgsMutation
    public AuthDto register(@InputArgument RegisterInput registerInput) {
        UserToken userToken = authService.register(registerInput);
        return new AuthDto(userToken.getToken(), userMapper.mapTo(userToken.getUser()));
    }
}
