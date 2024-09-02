package com.CptFranck.SportsPeak.domain.input.privilege;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InputPrivilege extends InputNewPrivilege {

    private Long id;

    public InputPrivilege(Long id, String name, ArrayList<Long> roleIds) {
        super(name, roleIds);
        this.id = id;
    }
}
