package com.CptFranck.SportsPeak.domain.exception.role;

public class RoleExistsException extends RuntimeException {
    public RoleExistsException() {
        super("A role with this name already exists");
    }
}
