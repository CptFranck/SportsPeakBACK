package com.CptFranck.SportsPeak.domain.input.user;

import lombok.Data;

@Data
public class InputRegisterNewUser {
    private String email;

    private String firstName;

    private String lastName;

    private String username;

    private String password;
}
