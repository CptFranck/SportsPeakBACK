package com.CptFranck.SportsPeak.domain.input.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InputRegisterNewUser {
    private String email;

    private String firstName;

    private String lastName;

    private String username;

    private String password;
}
