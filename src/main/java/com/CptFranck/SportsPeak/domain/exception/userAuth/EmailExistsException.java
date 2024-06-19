package com.CptFranck.SportsPeak.domain.exception.userAuth;

public class EmailExistsException extends RuntimeException {
    public EmailExistsException() {
        super("Email already used for an other account");
    }
}
