package com.CptFranck.SportsPeak.resolvers;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.role.InputNewRole;
import com.CptFranck.SportsPeak.domain.input.role.InputRole;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import com.CptFranck.SportsPeak.service.UserService;
import graphql.com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RoleInputResolver {

    private final UserService userService;
    private final PrivilegeService privilegeService;

    public RoleInputResolver(UserService userService, PrivilegeService privilegeService) {
        this.userService = userService;
        this.privilegeService = privilegeService;
    }

    public RoleEntity resolveInput(InputNewRole inputNewRole) {
        Set<Long> newUserIds = Sets.newHashSet(inputNewRole.getUserIds());
        Set<Long> privilegeIds = Sets.newHashSet(inputNewRole.getPrivilegeIds());

        Set<UserEntity> users = userService.findMany(newUserIds);
        Set<PrivilegeEntity> privileges = privilegeService.findMany(privilegeIds);

        Long id;
        if (inputNewRole instanceof InputRole) {
            id = ((InputRole) inputNewRole).getId();
        } else {
            id = null;
        }

        return new RoleEntity(
                id,
                inputNewRole.getName(),
                users,
                privileges
        );
    }
}
