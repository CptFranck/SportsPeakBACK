package com.CptFranck.SportsPeak.domain.exception.role;

public class RoleStillUsedByUserException extends RuntimeException {
    public RoleStillUsedByUserException(Long roleId, Long usersUsingRole) {
        super(String.format("The role with id %s is still used by %s user(s)", roleId.toString(), usersUsingRole.toString()));
    }
}
