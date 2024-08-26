package com.CptFranck.SportsPeak.domain.exception.privilege;

public class PrivilegeNotFoundException extends RuntimeException {
    public PrivilegeNotFoundException(Long id) {
        super(String.format("The privilege with the id %s has not been found", id.toString()));
    }
}
