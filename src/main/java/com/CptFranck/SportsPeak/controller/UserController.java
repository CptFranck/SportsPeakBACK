package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.user.InputNewUser;
import com.CptFranck.SportsPeak.domain.input.user.InputUser;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

@DgsComponent
public class UserController {

    private final RoleService roleService;
    private final UserService userService;
    private final AuthService authService;
    private final Mapper<UserEntity, UserDto> userMapper;

    public UserController(RoleService roleService, UserService userService, AuthService authService, Mapper<UserEntity, UserDto> userMapper) {
        this.roleService = roleService;
        this.userService = userService;
        this.authService = authService;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public UserDto addUser(@InputArgument InputNewUser inputNewUser) {
        return userMapper.mapTo(authService.register(this.inputToEntity(inputNewUser)));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public UserDto modifyUser(@InputArgument InputUser inputUser) {
        if (!userService.exists(inputUser.getId())) {
            return null;
        }
        return userMapper.mapTo(inputToEntity(inputUser));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public Long deleteUser(@InputArgument Long userId) {
        if (!userService.exists(userId)) {
            return null;
        }
        userService.delete(userId);
        return userId;
    }

    private UserEntity inputToEntity(InputNewUser inputNewUser) {
        Set<Long> roleIds = Sets.newHashSet((inputNewUser.getRoleIds()));
        Set<RoleEntity> roles = roleService.findMany(roleIds);

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
                inputNewUser.getPassword(),
                roles
        );
    }
}
