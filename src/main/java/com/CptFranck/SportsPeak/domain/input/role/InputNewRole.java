package com.CptFranck.SportsPeak.domain.input.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InputNewRole {

    private String name;

    private List<Long> userIds;

    private List<Long> privilegeIds;
}
