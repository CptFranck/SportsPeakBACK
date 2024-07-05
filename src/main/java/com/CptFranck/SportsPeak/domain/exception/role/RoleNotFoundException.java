package com.CptFranck.SportsPeak.domain.exception.role;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String role) {
        super(String.format("The role %s has not been found", role));
    }
}
