package com.CptFranck.SportsPeak.domain.input.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InputUserUsername {

    private Long id;

    private String newUsername;
}
