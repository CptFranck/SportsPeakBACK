package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.security.JwtProvider;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@DgsComponent
public class UserController {

    private final RoleService roleService;
    private final UserService userService;
    private final JwtProvider userAuthProvider;
    private final Mapper<UserEntity, UserDto> userMapper;
    private final AuthenticationManager authenticationManager;

    public UserController(RoleService roleService, UserService userService, JwtProvider userAuthProvider, Mapper<UserEntity, UserDto> userMapper, AuthenticationManager authenticationManager) {
        this.roleService = roleService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.userAuthProvider = userAuthProvider;
        this.authenticationManager = authenticationManager;
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
    @DgsQuery
    public List<ProgExerciseDto> getUserProgExercises(@InputArgument Long userId) {
        Optional<UserEntity> userEntity = userService.findOne(userId);
        UserDto user = userEntity.map(userMapper::mapTo).orElse(null);
        if (user != null) {
            return user.getSubscribedProgExercises().stream().toList();
        }
        return new LinkedList<>();
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
    public AuthDto modifyUserEmail(@InputArgument InputUserEmail inputUserEmail) {
        UserEntity userEnt = userService.changeEmail(
                inputUserEmail.getId(),
                inputUserEmail.getPassword(),
                inputUserEmail.getNewEmail()
        );

        UserDto user = userMapper.mapTo(userEnt);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(inputUserEmail.getNewEmail(), inputUserEmail.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new AuthDto(userAuthProvider.generateToken(authentication), user);
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
