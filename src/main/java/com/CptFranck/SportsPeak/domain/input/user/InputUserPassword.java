package com.CptFranck.SportsPeak.domain.input.user;

import lombok.Data;

@Data
public class InputUserPassword {

    private Long id;

    private String oldPassword;

    private String newPassword;
}
