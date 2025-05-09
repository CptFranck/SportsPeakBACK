package com.CptFranck.SportsPeak.domain.input.privilege;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InputNewPrivilege {

    private String name;

    private List<Long> roleIds;
}
