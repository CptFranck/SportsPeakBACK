package com.CptFranck.SportsPeak.domain.exception.userAuth;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found");
    }
}
