package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.user.*;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.com.google.common.collect.Sets;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@DgsComponent
public class UserController {

    private final RoleService roleService;
    private final UserService userService;
    private final Mapper<UserEntity, UserDto> userMapper;

    public UserController(RoleService roleService, UserService userService, Mapper<UserEntity, UserDto> userMapper) {
        this.roleService = roleService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsQuery
    public List<UserDto> getUsers() {
        return userService.findAll().stream().map(userMapper::mapTo).toList();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsQuery
    public UserDto getUserById(@InputArgument Long id) {
        Optional<UserEntity> userEntity = userService.findOne(id);
        return userEntity.map(userMapper::mapTo).orElse(null);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public UserDto modifyUserIdentity(@InputArgument InputUserIdentity inputUserIdentity) {
        UserEntity userEntity = userService.changeIdentity(inputUserIdentity.getId(),
                inputUserIdentity.getFirstName(),
                inputUserIdentity.getLastName());
        return userMapper.mapTo(userEntity);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public UserDto modifyUserRoles(@InputArgument InputUserRoles inputUserRoles) {
        Set<Long> roleIds = Sets.newHashSet((inputUserRoles.getRoleIds()));
        Set<RoleEntity> roles = roleService.findMany(roleIds);
        UserEntity userEntity = userService.changeRoles(inputUserRoles.getId(), roles);
        return userMapper.mapTo(userEntity);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public UserDto modifyUserEmail(@InputArgument InputUserEmail inputUserEmail) {
        UserEntity userEntity = userService.changeEmail(inputUserEmail.getId(), inputUserEmail.getPassword(), inputUserEmail.getNewEmail());
        return userMapper.mapTo(userEntity);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public UserDto modifyUserUsername(@InputArgument InputUserUsername inputUserUsername) {
        UserEntity userEntity = userService.changeUsername(inputUserUsername.getId(), inputUserUsername.getNewUsername());
        return userMapper.mapTo(userEntity);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public UserDto modifyUserPassword(@InputArgument InputUserPassword inputUserPassword) {
        UserEntity userEntity = userService.changePassword(inputUserPassword.getId(), inputUserPassword.getOldPassword(), inputUserPassword.getNewPassword());
        return userMapper.mapTo(userEntity);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public Long deleteUser(@InputArgument Long userId) {
        if (!userService.exists(userId)) {
            return null;
        }
        userService.delete(userId);
        return userId;
    }
}
