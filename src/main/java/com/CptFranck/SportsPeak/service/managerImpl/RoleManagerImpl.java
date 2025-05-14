package com.CptFranck.SportsPeak.service.managerImpl;

import com.CptFranck.SportsPeak.domain.entity.PrivilegeEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.service.PrivilegeService;
import com.CptFranck.SportsPeak.service.RoleManager;
import com.CptFranck.SportsPeak.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleManagerImpl implements RoleManager {

    private final RoleService roleService;

    private final PrivilegeService privilegeService;

    public RoleManagerImpl(RoleService roleService, PrivilegeService privilegeService) {
        this.roleService = roleService;
        this.privilegeService = privilegeService;
    }

    @Override
    public PrivilegeEntity savePrivilege(PrivilegeEntity privilege) {
        Set<Long> oldRoleIds;
        if (privilege.getId() == null)
            oldRoleIds = Collections.emptySet();
        else
            oldRoleIds = privilegeService.findOne(privilege.getId()).getRoles()
                    .stream().map(RoleEntity::getId).collect(Collectors.toSet());

        Set<Long> newRoleIds = privilege.getRoles()
                .stream().map(RoleEntity::getId).collect(Collectors.toSet());

        PrivilegeEntity privilegeSave = privilegeService.save(privilege);

        roleService.updatePrivilegeRelation(newRoleIds, oldRoleIds, privilegeSave);

        return privilegeSave;
    }
}
