package com.CptFranck.SportsPeak.utils;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.user.InputRegisterNewUser;

public class AuthUtils {
    public static InputCredentials createInputCredentials(UserEntity user) {
        return new InputCredentials(
                user.getEmail(),
                user.getPassword()
        );
    }

    public static InputRegisterNewUser createInputRegisterNewUser(UserEntity user) {
        return new InputRegisterNewUser(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getPassword());
    }
}
