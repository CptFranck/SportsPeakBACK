package com.CptFranck.SportsPeak.domain.exception.role;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String idOrRole) {
        super(String.format("The role id or name %s has not been found", idOrRole));
    }
}
