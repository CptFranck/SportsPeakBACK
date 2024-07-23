package com.CptFranck.SportsPeak.domain.exception.role;

public class RoleMissingException extends RuntimeException {
    public RoleMissingException() {
        super("A role has not been found in set");
    }
}
