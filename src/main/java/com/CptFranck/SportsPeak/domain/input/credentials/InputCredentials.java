package com.CptFranck.SportsPeak.domain.input.credentials;

import lombok.Data;

@Data
public class InputCredentials {

    private String login;

    private char[] password;
}
