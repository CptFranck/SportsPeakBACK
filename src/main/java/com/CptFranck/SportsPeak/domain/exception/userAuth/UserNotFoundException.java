package com.CptFranck.SportsPeak.domain.exception.userAuth;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super(String.format("User with id %s has been not found", id.toString()));
    }
}
