package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.role.InputNewRole;
import com.CptFranck.SportsPeak.domain.input.role.InputRole;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.com.google.common.collect.Sets;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@DgsComponent
public class RoleController {

    private final UserService userService;
    private final RoleService roleService;
    private final PrivilegeService privilegeService;
    private final Mapper<RoleEntity, RoleDto> roleMapper;

    public RoleController(UserService userService, RoleService roleService, PrivilegeService privilegeService, Mapper<RoleEntity, RoleDto> roleMapper) {
        this.userService = userService;
        this.roleService = roleService;
        this.privilegeService = privilegeService;
        this.roleMapper = roleMapper;
    }

    @PreAuthorize("hasAuthority('STAFF')")
    @DgsQuery
    public List<RoleDto> getRoles() {
        return roleService.findAll().stream().map(roleMapper::mapTo).toList();
    }

    @DgsQuery
    public RoleDto getRoleById(@InputArgument Long id) {
        Optional<RoleEntity> muscleEntity = roleService.findOne(id);
        return muscleEntity.map(roleMapper::mapTo).orElse(null);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DgsMutation
    public RoleDto addRole(@InputArgument InputNewRole inputNewRole) {
        System.out.println(inputNewRole);
        return roleMapper.mapTo(inputToEntity(inputNewRole));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DgsMutation
    public RoleDto modifyRole(@InputArgument InputRole inputRole) {
        if (!roleService.exists(inputRole.getId())) {
            return null;
        }
        return roleMapper.mapTo(inputToEntity(inputRole));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DgsMutation
    public Long deleteRole(@InputArgument Long roleId) {
        if (!roleService.exists(roleId)) {
            return null;
        }
        roleService.delete(roleId);
        return roleId;
    }

    private RoleEntity inputToEntity(InputNewRole inputNewRole) {
        Set<Long> oldUserIds = new HashSet<>();
        Set<Long> newUserIds = Sets.newHashSet(inputNewRole.getUserIds());
        Set<Long> privilegeIds = Sets.newHashSet(inputNewRole.getPrivilegeIds());

        Set<UserEntity> users = userService.findMany(newUserIds);
        Set<PrivilegeEntity> privileges = privilegeService.findMany(privilegeIds);

        Long id = null;
        if (inputNewRole instanceof InputRole) {
            id = ((InputRole) inputNewRole).getId();
            Optional<RoleEntity> roles = roleService.findOne(id);
            roles.ifPresent(roleEntity ->
                    roleEntity.getUsers().forEach(u -> oldUserIds.add(u.getId())));
        }
        RoleEntity role = new RoleEntity(
                id,
                inputNewRole.getName(),
                users,
                privileges
        );

        roleService.save(role);
        userService.updateRoleRelation(newUserIds, oldUserIds, role);
        return role;
    }
}
