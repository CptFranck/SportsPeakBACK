package com.CptFranck.SportsPeak.domain.input.role;

import lombok.Data;

import java.util.List;

@Data
public class InputNewRole {

    private String name;

    private List<Long> userIds;

    private List<Long> privilegeIds;
}
