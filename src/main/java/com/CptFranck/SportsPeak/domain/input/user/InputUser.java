package com.CptFranck.SportsPeak.domain.input.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InputUser extends InputNewUser {

    private Long id;

}
