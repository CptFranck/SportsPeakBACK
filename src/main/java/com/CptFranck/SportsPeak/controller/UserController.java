package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.user.*;
import com.CptFranck.SportsPeak.domain.model.UserTokens;
import com.CptFranck.SportsPeak.mapper.Mapper;
import com.CptFranck.SportsPeak.resolver.UserInputResolver;
import com.CptFranck.SportsPeak.security.RefreshTokenCookieHandler;
import com.CptFranck.SportsPeak.service.AuthService;
import com.CptFranck.SportsPeak.service.UserManager;
import com.CptFranck.SportsPeak.service.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.List;

@DgsComponent
public class UserController {

    private final UserManager userManager;

    private final UserService userService;

    private final AuthService authService;

    private final UserInputResolver userInputResolver;

    private final RefreshTokenCookieHandler refreshTokenCookieHandler;

    private final Mapper<UserEntity, UserDto> userMapper;

    private final Mapper<ProgExerciseEntity, ProgExerciseDto> progExerciseMapper;

    public UserController(UserManager userManager, UserInputResolver userInputResolver, UserService userService, Mapper<UserEntity, UserDto> userMapper, Mapper<ProgExerciseEntity, ProgExerciseDto> progExerciseMapper, AuthenticationManager authenticationManager, AuthService authService, RefreshTokenCookieHandler refreshTokenCookieHandler) {
        this.userMapper = userMapper;
        this.userManager = userManager;
        this.userService = userService;
        this.authService = authService;
        this.userInputResolver = userInputResolver;
        this.progExerciseMapper = progExerciseMapper;
        this.refreshTokenCookieHandler = refreshTokenCookieHandler;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsQuery
    public List<UserDto> getUsers() {
        return userService.findAll().stream().map(userMapper::mapTo).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsQuery
    public UserDto getUserById(@InputArgument Long id) {
        UserEntity userEntity = userService.findOne(id);
        return userMapper.mapTo(userEntity);
    }

    @PreAuthorize("hasRole('USER')")
    @DgsQuery
    public List<ProgExerciseDto> getUserProgExercises(@InputArgument Long userId) {
        UserEntity userEntity = userService.findOne(userId);
        return userEntity.getSubscribedProgExercises().stream().map(progExerciseMapper::mapTo).toList();
    }

    @PreAuthorize("hasRole('USER')")
    @DgsMutation
    public UserDto modifyUserIdentity(@InputArgument InputUserIdentity inputUserIdentity) {
        UserEntity userEntity = userInputResolver.resolveInput(inputUserIdentity);
        return userMapper.mapTo(userService.save(userEntity));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public UserDto modifyUserRoles(@InputArgument InputUserRoles inputUserRoles) {
        UserEntity userEntity = userManager.updateUserRoles(inputUserRoles);
        return userMapper.mapTo(userEntity);
    }

    @PreAuthorize("hasRole('USER')")
    @DgsMutation
    public AuthDto modifyUserEmail(@InputArgument InputUserEmail inputUserEmail) {
        UserTokens userToken = authService.updateEmail(inputUserEmail);

        refreshTokenCookieHandler.addRefreshTokenToCookie(userToken.getRefreshToken());

        return new AuthDto(userToken.getAccessToken(), userMapper.mapTo(userToken.getUser()));
    }

    @PreAuthorize("hasRole('USER')")
    @DgsMutation
    public UserDto modifyUserUsername(@InputArgument InputUserUsername inputUserUsername) {
        UserEntity userEntity = userInputResolver.resolveInput(inputUserUsername);
        return userMapper.mapTo(userService.save(userEntity));
    }

    @PreAuthorize("hasRole('USER')")
    @DgsMutation
    public AuthDto modifyUserPassword(@InputArgument InputUserPassword inputUserPassword) {
        UserTokens userToken = authService.updatePassword(inputUserPassword);

        refreshTokenCookieHandler.addRefreshTokenToCookie(userToken.getRefreshToken());

        return new AuthDto(userToken.getAccessToken(), userMapper.mapTo(userToken.getUser()));
    }

    @PreAuthorize("hasRole('USER')")
    @DgsMutation
    public Long deleteUser(@InputArgument Long userId) {
        userService.delete(userId);
        return userId;
    }
}
