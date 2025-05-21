package com.CptFranck.SportsPeak.resolver;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.input.privilege.InputNewPrivilege;
import com.CptFranck.SportsPeak.domain.input.privilege.InputPrivilege;
import com.CptFranck.SportsPeak.service.RoleService;
import graphql.com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PrivilegeInputResolver {

    private final RoleService roleService;

    public PrivilegeInputResolver(RoleService roleService) {
        this.roleService = roleService;
    }

    public PrivilegeEntity resolveInput(InputNewPrivilege inputNewPrivilege) {
        Set<Long> newRoleIds = Sets.newHashSet(inputNewPrivilege.getRoleIds());
        Set<RoleEntity> roles = roleService.findMany(newRoleIds);

        Long id;
        if (inputNewPrivilege instanceof InputPrivilege) {
            id = ((InputPrivilege) inputNewPrivilege).getId();
        } else {
            id = null;
        }

        return new PrivilegeEntity(
                id,
                inputNewPrivilege.getName(),
                roles
        );
    }
}
