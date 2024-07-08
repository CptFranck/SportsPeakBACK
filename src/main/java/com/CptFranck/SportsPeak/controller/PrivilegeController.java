package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.input.privilege.InputNewPrivilege;
import com.CptFranck.SportsPeak.domain.input.privilege.InputPrivilege;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import com.CptFranck.SportsPeak.service.RoleService;
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
public class PrivilegeController {

    private final RoleService roleService;
    private final PrivilegeService privilegeService;
    private final Mapper<PrivilegeEntity, PrivilegeDto> privilegeMapper;

    public PrivilegeController(RoleService exerciseService, PrivilegeService privilegeService, Mapper<PrivilegeEntity, PrivilegeDto> privilegeMapper) {
        this.roleService = exerciseService;
        this.privilegeService = privilegeService;
        this.privilegeMapper = privilegeMapper;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsQuery
    public List<PrivilegeDto> getPrivileges() {
        return privilegeService.findAll().stream().map(privilegeMapper::mapTo).toList();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsQuery
    public PrivilegeDto getPrivilegeById(@InputArgument Long id) {
        Optional<PrivilegeEntity> privilege = privilegeService.findOne(id);
        return privilege.map(privilegeMapper::mapTo).orElse(null);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public PrivilegeDto addPrivilege(@InputArgument InputNewPrivilege inputNewPrivilege) {
        return privilegeMapper.mapTo(inputToEntity(inputNewPrivilege));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public PrivilegeDto modifyPrivilege(@InputArgument InputPrivilege inputPrivilege) {
        if (!privilegeService.exists(inputPrivilege.getId())) {
            return null;
        }
        return privilegeMapper.mapTo(inputToEntity(inputPrivilege));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DgsMutation
    public Long deletePrivilege(@InputArgument Long privilegeId) {
        if (!privilegeService.exists(privilegeId)) {
            return null;
        }
        privilegeService.delete(privilegeId);
        return privilegeId;
    }

    private PrivilegeEntity inputToEntity(InputNewPrivilege inputNewPrivilege) {
        Set<Long> oldRoleIds = new HashSet<>();
        Set<Long> newRoleIds = Sets.newHashSet(inputNewPrivilege.getRoleIds());

        Set<RoleEntity> roles = roleService.findMany(newRoleIds);

        Long id = null;
        if (inputNewPrivilege instanceof InputPrivilege) {
            id = ((InputPrivilege) inputNewPrivilege).getId();
            Optional<PrivilegeEntity> privilege = privilegeService.findOne(id);
            privilege.ifPresent(privilegeEntity ->
                    privilegeEntity.getRoles().forEach(r -> oldRoleIds.add(r.getId())));
        }
        PrivilegeEntity privilege = new PrivilegeEntity(
                id,
                inputNewPrivilege.getName(),
                roles
        );

        privilege = privilegeService.save(privilege);
        roleService.updatePrivilegeRelation(newRoleIds, oldRoleIds, privilege);
        return privilege;
    }
}
