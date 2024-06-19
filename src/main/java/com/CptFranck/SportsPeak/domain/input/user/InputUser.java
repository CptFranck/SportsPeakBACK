package com.CptFranck.SportsPeak.domain.input.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class InputUser extends InputNewUser {

    private Long id;

    private List<Long> roleIds;

}
