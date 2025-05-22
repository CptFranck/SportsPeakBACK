package com.CptFranck.SportsPeak.domain.exception.privilege;

public class PrivilegeStillUsedByRoleException extends RuntimeException {
    public PrivilegeStillUsedByRoleException(Long privilegeId, Long roleUsingPrivilege) {
        super(String.format("The privilege with id %s is still used by %s role(s)", privilegeId.toString(), roleUsingPrivilege.toString()));
    }
}
