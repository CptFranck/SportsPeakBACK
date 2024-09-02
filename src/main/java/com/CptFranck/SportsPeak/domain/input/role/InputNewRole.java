package com.CptFranck.SportsPeak.domain.input.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InputNewRole {

    private String name;

    private List<Long> userIds;

    private List<Long> privilegeIds;
}
