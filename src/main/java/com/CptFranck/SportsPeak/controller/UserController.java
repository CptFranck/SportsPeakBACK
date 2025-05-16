package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.user.*;
import com.CptFranck.SportsPeak.domain.model.UserToken;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.AuthService;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.com.google.common.collect.Sets;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.List;
import java.util.Set;

@DgsComponent
public class UserController {

    private final RoleService roleService;
    private final UserService userService;
    private final AuthService authService;
    private final Mapper<UserEntity, UserDto> userMapper;
    private final Mapper<ProgExerciseEntity, ProgExerciseDto> progExerciseMapper;

    public UserController(RoleService roleService, UserService userService, Mapper<UserEntity, UserDto> userMapper, Mapper<ProgExerciseEntity, ProgExerciseDto> progExerciseMapper, AuthenticationManager authenticationManager, AuthService authService) {
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.userService = userService;
        this.authService = authService;
        this.progExerciseMapper = progExerciseMapper;
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
        UserEntity userEntity = userService.changeIdentity(inputUserIdentity.getId(),
                inputUserIdentity.getFirstName(),
                inputUserIdentity.getLastName());
        return userMapper.mapTo(userEntity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public UserDto modifyUserRoles(@InputArgument InputUserRoles inputUserRoles) {
        Set<Long> roleIds = Sets.newHashSet((inputUserRoles.getRoleIds()));
        Set<RoleEntity> roles = roleService.findMany(roleIds);
        UserEntity userEntity = userService.changeRoles(inputUserRoles.getId(), roles);
        return userMapper.mapTo(userEntity);
    }

    @PreAuthorize("hasRole('USER')")
    @DgsMutation
    public AuthDto modifyUserEmail(@InputArgument InputUserEmail inputUserEmail) {
        UserToken userToken = authService.updateEmail(inputUserEmail);
        return new AuthDto(userToken.getToken(), userMapper.mapTo(userToken.getUser()));
    }

    @PreAuthorize("hasRole('USER')")
    @DgsMutation
    public UserDto modifyUserUsername(@InputArgument InputUserUsername inputUserUsername) {
        UserEntity userEntity = userService.changeUsername(inputUserUsername.getId(), inputUserUsername.getNewUsername());
        return userMapper.mapTo(userEntity);
    }

    @PreAuthorize("hasRole('USER')")
    @DgsMutation
    public AuthDto modifyUserPassword(@InputArgument InputUserPassword inputUserPassword) {
        UserToken userToken = authService.updatePassword(inputUserPassword);
        return new AuthDto(userToken.getToken(), userMapper.mapTo(userToken.getUser()));
    }

    @PreAuthorize("hasRole('USER')")
    @DgsMutation
    public Long deleteUser(@InputArgument Long userId) {
        userService.delete(userId);
        return userId;
    }
}
