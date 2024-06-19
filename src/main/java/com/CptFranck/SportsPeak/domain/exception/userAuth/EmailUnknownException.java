package com.CptFranck.SportsPeak.domain.exception.userAuth;

public class EmailUnknownException extends RuntimeException {
    public EmailUnknownException() {
        super("Email unknown");
    }
}
