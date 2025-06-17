package com.CptFranck.SportsPeak.domain.exception.token;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
