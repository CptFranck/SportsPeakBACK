package com.CptFranck.SportsPeak.domain.exception.userAuth;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(Throwable cause) {
        super("Invalid credentials", cause);
    }
}
