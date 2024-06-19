package com.CptFranck.SportsPeak.domain.exception.privilege;

public class PrivilegeExistsException extends RuntimeException {
    public PrivilegeExistsException() {
        super("A privilege with this name already exists");
    }
}
