package com.CptFranck.SportsPeak.domain.exception.token;

public class TokenMissingException extends RuntimeException {
    public TokenMissingException(String message) {
        super(message);
    }
}
