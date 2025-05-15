package com.CptFranck.SportsPeak.domain.exception.userAuth;

public class EmailUnknownException extends RuntimeException {
    public EmailUnknownException(String email) {
        super(String.format("Email %s unknown", email));
    }
}
