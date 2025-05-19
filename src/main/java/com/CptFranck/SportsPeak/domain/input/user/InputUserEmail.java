package com.CptFranck.SportsPeak.domain.input.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InputUserEmail {

    private Long id;

    private String password;

    private String newEmail;
}
