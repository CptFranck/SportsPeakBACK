package com.CptFranck.SportsPeak.domain.input.privilege;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class InputPrivilege extends InputNewPrivilege {

    private Long id;

    public InputPrivilege(Long id, String name, ArrayList<Long> roleIds) {
        super(name, roleIds);
        this.id = id;
    }
}
