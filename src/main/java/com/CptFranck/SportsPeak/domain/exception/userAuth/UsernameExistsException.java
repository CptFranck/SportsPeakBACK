package com.CptFranck.SportsPeak.domain.exception.userAuth;

public class UsernameExistsException extends RuntimeException {
    public UsernameExistsException() {
        super("Username already used for an other account");
    }
}
