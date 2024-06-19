package com.CptFranck.SportsPeak.domain.input.privilege;

import lombok.Data;

import java.util.List;

@Data
public class InputNewPrivilege {

    private String name;

    private List<Long> roleIds;
}
