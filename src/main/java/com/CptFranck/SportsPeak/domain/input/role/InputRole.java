package com.CptFranck.SportsPeak.domain.input.role;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class InputRole extends InputNewRole {

    private Long id;

    public InputRole(Long id, String name, ArrayList<Long> userIds, ArrayList<Long> privilegeIds) {
        super(name, userIds, privilegeIds);
        this.id = id;
    }
}
