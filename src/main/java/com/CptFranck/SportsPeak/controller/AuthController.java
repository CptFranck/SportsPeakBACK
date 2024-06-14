package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.user.InputNewUser;
import com.CptFranck.SportsPeak.domain.input.user.InputUser;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.AuthService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.nio.CharBuffer;

@DgsComponent
public class AuthController {

    private final AuthService authService;
    private final Mapper<UserEntity, UserDto> userMapper;

    public AuthController(AuthService authService, Mapper<UserEntity, UserDto> userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @DgsQuery
    public UserDto login(@InputArgument InputCredentials inputCredentials) throws Exception {
        return userMapper.mapTo(authService.login(inputCredentials));
    }

    @DgsMutation
    public UserDto register(@InputArgument InputNewUser inputNewUser) {
        return userMapper.mapTo(authService.register(this.inputToEntity(inputNewUser)));
    }

    private UserEntity inputToEntity(InputNewUser inputNewUser) {
        Long id = null;
        if (inputNewUser instanceof InputUser) {
            id = ((InputUser) inputNewUser).getId();
        }
        return new UserEntity(
                id,
                inputNewUser.getEmail(),
                inputNewUser.getFirstName(),
                inputNewUser.getLastName(),
                inputNewUser.getUsername(),
                CharBuffer.wrap(inputNewUser.getPassword()).toString()
        );
    }
}
