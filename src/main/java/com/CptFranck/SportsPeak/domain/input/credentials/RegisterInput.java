package com.CptFranck.SportsPeak.domain.input.credentials;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterInput {
    private String email;

    private String firstName;

    private String lastName;

    private String username;

    private String password;
}
