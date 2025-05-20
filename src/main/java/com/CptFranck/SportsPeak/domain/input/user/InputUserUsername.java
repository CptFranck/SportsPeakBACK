package com.CptFranck.SportsPeak.domain.input.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
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
