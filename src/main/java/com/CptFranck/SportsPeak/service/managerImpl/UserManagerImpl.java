package com.CptFranck.SportsPeak.service.managerImpl;

import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.user.InputUserRoles;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.UserManager;
import com.CptFranck.SportsPeak.service.UserService;
import graphql.com.google.common.collect.Sets;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserManagerImpl implements UserManager {

    private final UserService userService;

    private final RoleService roleService;

    public UserManagerImpl(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public RoleEntity saveRole(RoleEntity role) {
        Set<Long> oldUserIds;
        if (role.getId() == null)
            oldUserIds = Collections.emptySet();
        else
            oldUserIds = roleService.findOne(role.getId()).getUsers()
                    .stream().map(UserEntity::getId).collect(Collectors.toSet());

        Set<Long> newUserIds = role.getUsers()
                .stream().map(UserEntity::getId).collect(Collectors.toSet());

        RoleEntity roleSaved = roleService.save(role);

        userService.updateRoleRelation(newUserIds, oldUserIds, role);

        return roleSaved;
    }

    @Override
    public UserEntity updateUserRoles(InputUserRoles inputUserRoles) {
        UserEntity user = userService.findOne(inputUserRoles.getId());
        Set<Long> roleIds = Sets.newHashSet((inputUserRoles.getRoleIds()));
        Set<RoleEntity> roles = roleService.findMany(roleIds);
        return userService.changeRoles(user, roles);
    }
}
