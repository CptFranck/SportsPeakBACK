package com.CptFranck.SportsPeak.service;

import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.user.InputUserRoles;

public interface UserManager {

    RoleEntity saveRole(RoleEntity role);

    UserEntity updateUserRoles(InputUserRoles inputUserRoles);
}
