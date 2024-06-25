package com.CptFranck.SportsPeak.domain.exception.userAuth;

public class UsernameUnknownException extends RuntimeException {
    public UsernameUnknownException() {
        super("Username unknown");
    }
}
