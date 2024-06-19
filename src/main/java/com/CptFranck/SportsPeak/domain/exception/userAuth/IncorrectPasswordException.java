package com.CptFranck.SportsPeak.domain.exception.userAuth;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException() {
        super("Incorrect password");
    }
}
