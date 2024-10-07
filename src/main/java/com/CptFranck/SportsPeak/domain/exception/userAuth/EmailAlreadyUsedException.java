package com.CptFranck.SportsPeak.domain.exception.userAuth;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException() {
        super("Email already used for an other account");
    }
}
