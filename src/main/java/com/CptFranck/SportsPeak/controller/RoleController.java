package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsQuery
    public List<RoleDto> getRoles() {
        return roleService.findAll().stream().map(roleMapper::mapTo).toList();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsQuery
    public RoleDto getRoleById(@InputArgument Long id) {
        RoleEntity role = roleService.findOne(id).orElseThrow(() -> new RoleNotFoundException(id.toString()));
        ;
        return roleMapper.mapTo(role);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public RoleDto addRole(@InputArgument InputNewRole inputNewRole) {
        return roleMapper.mapTo(inputToEntity(inputNewRole));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public RoleDto modifyRole(@InputArgument InputRole inputRole) {
        return roleMapper.mapTo(inputToEntity(inputRole));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public Long deleteRole(@InputArgument Long roleId) {
        roleService.delete(roleId);
        return roleId;
    }

    private RoleEntity inputToEntity(InputNewRole inputNewRole) {
        Set<Long> oldUserIds = new HashSet<>();
        Set<Long> newUserIds = Sets.newHashSet(inputNewRole.getUserIds());
        Set<Long> privilegeIds = Sets.newHashSet(inputNewRole.getPrivilegeIds());

        Set<UserEntity> users = userService.findMany(newUserIds);
        Set<PrivilegeEntity> privileges = privilegeService.findMany(privilegeIds);

        Long id;
        if (inputNewRole instanceof InputRole) {
            id = ((InputRole) inputNewRole).getId();
            RoleEntity roles = roleService.findOne(id).orElseThrow(() -> new RoleNotFoundException(id.toString()));
            roles.getUsers().forEach(u -> oldUserIds.add(u.getId()));
        } else {
            id = null;
        }
        RoleEntity role = new RoleEntity(
                id,
                inputNewRole.getName(),
                users,
                privileges
        );

        role = roleService.save(role);
        userService.updateRoleRelation(newUserIds, oldUserIds, role);
        return role;
    }
}
