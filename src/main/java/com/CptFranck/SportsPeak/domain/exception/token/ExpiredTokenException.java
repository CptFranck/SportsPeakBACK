package com.CptFranck.SportsPeak.domain.exception.token;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException(String message) {
        super(message);
    }
}
