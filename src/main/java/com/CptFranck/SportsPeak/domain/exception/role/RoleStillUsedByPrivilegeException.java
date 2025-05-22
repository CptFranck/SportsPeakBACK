package com.CptFranck.SportsPeak.domain.exception.role;

public class RoleStillUsedByPrivilegeException extends RuntimeException {
    public RoleStillUsedByPrivilegeException(Long roleId, Long privilegesUsingRole) {
        super(String.format("The role with id %s is still used by %s privileges(s)", roleId.toString(), privilegesUsingRole.toString()));
    }
}
