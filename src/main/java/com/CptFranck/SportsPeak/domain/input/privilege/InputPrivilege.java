package com.CptFranck.SportsPeak.domain.input.privilege;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InputPrivilege extends InputNewPrivilege {

    private Long id;

}
