package com.CptFranck.SportsPeak.domain.exception.token;

public class TokenUsernameInvalidException extends RuntimeException {
    public TokenUsernameInvalidException(String message) {
        super("Token not found");
    }
}
