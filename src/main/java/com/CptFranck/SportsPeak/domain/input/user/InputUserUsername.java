package com.CptFranck.SportsPeak.domain.input.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InputUserUsername extends AbstractInputUser {

    private String newUsername;

    public InputUserUsername(Long id, String newUsername) {
        super(id);
        this.newUsername = newUsername;
    }
}
