package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.PrivilegeDto;
import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.input.privilege.InputNewPrivilege;
import com.CptFranck.SportsPeak.domain.input.privilege.InputPrivilege;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.resolver.PrivilegeInputResolver;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import com.CptFranck.SportsPeak.service.RoleManager;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@DgsComponent
public class PrivilegeController {

    private final RoleManager roleManager;

    private final PrivilegeService privilegeService;

    private final PrivilegeInputResolver privilegeInputResolver;

    private final Mapper<PrivilegeEntity, PrivilegeDto> privilegeMapper;

    public PrivilegeController(RoleManager roleManager, PrivilegeService privilegeService, PrivilegeInputResolver privilegeInputResolver, Mapper<PrivilegeEntity, PrivilegeDto> privilegeMapper) {
        this.roleManager = roleManager;
        this.privilegeMapper = privilegeMapper;
        this.privilegeService = privilegeService;
        this.privilegeInputResolver = privilegeInputResolver;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsQuery
    public List<PrivilegeDto> getPrivileges() {
        return privilegeService.findAll().stream().map(privilegeMapper::mapTo).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsQuery
    public PrivilegeDto getPrivilegeById(@InputArgument Long id) {
        PrivilegeEntity privilege = privilegeService.findOne(id);
        return privilegeMapper.mapTo(privilege);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public PrivilegeDto addPrivilege(@InputArgument InputNewPrivilege inputNewPrivilege) {
        PrivilegeEntity privilege = privilegeInputResolver.resolveInput(inputNewPrivilege);
        return privilegeMapper.mapTo(roleManager.savePrivilege(privilege));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public PrivilegeDto modifyPrivilege(@InputArgument InputPrivilege inputPrivilege) {
        PrivilegeEntity privilege = privilegeInputResolver.resolveInput(inputPrivilege);
        return privilegeMapper.mapTo(roleManager.savePrivilege(privilege));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation
    public Long deletePrivilege(@InputArgument Long privilegeId) {
        privilegeService.delete(privilegeId);
        return privilegeId;
    }
}
