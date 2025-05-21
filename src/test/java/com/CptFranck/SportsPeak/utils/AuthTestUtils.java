package com.CptFranck.SportsPeak.utils;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.credentials.RegisterInput;

public class AuthTestUtils {
    public static InputCredentials createInputCredentials(UserEntity user) {
        return new InputCredentials(
                user.getEmail(),
                user.getPassword()
        );
    }

    public static RegisterInput createRegisterInput(UserEntity user) {
        return new RegisterInput(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getPassword());
    }
}
