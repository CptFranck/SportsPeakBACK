package com.CptFranck.SportsPeak.domain.input.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InputUserIdentity extends AbstractInputUser {

    private String firstName;

    private String lastName;

    public InputUserIdentity(Long id, String firstName, String lastName) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
