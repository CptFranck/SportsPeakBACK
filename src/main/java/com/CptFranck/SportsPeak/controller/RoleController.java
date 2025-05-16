package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.RoleDto;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.input.role.InputNewRole;
import com.CptFranck.SportsPeak.domain.input.role.InputRole;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.resolvers.RoleInputResolver;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.UserManager;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@DgsComponent
public class RoleController {

    private final UserManager userManager;
    private final RoleService roleService;
    private final RoleInputResolver roleInputResolver;
    private final Mapper<RoleEntity, RoleDto> roleMapper;

    public RoleController(UserManager userManager, RoleService roleService, RoleInputResolver roleInputResolver, Mapper<RoleEntity, RoleDto> roleMapper) {
        this.roleMapper = roleMapper;
        this.userManager = userManager;
        this.roleService = roleService;
        this.roleInputResolver = roleInputResolver;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsQuery
    public List<RoleDto> getRoles() {
        return roleService.findAll().stream().map(roleMapper::mapTo).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsQuery
    public RoleDto getRoleById(@InputArgument Long id) {
        RoleEntity role = roleService.findOne(id);
        return roleMapper.mapTo(role);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public RoleDto addRole(@InputArgument InputNewRole inputNewRole) {
        RoleEntity role = roleInputResolver.resolveInput(inputNewRole);
        return roleMapper.mapTo(userManager.saveRole(role));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public RoleDto modifyRole(@InputArgument InputRole inputRole) {
        RoleEntity role = roleInputResolver.resolveInput(inputRole);
        return roleMapper.mapTo(userManager.saveRole(role));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public Long deleteRole(@InputArgument Long roleId) {
        roleService.delete(roleId);
        return roleId;
    }
}
