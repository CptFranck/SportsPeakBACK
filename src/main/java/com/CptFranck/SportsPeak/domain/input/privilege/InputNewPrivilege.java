package com.CptFranck.SportsPeak.domain.input.privilege;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InputNewPrivilege {

    private String name;

    private List<Long> roleIds;
}
