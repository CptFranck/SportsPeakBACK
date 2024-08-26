package com.CptFranck.SportsPeak.domain.exception.role;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String idOrRole) {
        super(String.format("The role the id or the name %s has not been found", idOrRole));
    }
}
