package com.CptFranck.SportsPeak.domain.exception.token;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException() {
        super("Token not found");
    }
}
