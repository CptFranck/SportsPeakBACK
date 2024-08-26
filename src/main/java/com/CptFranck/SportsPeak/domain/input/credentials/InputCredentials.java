package com.CptFranck.SportsPeak.domain.input.credentials;

import lombok.Data;

@Data
public class InputCredentials {

    private String email;

    private String password;

    public InputCredentials(String mail, String password) {
        this.email = mail;
        this.password = password;
    }
}
