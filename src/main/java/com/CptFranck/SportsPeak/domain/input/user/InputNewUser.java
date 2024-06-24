package com.CptFranck.SportsPeak.domain.input.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
@Data
@EqualsAndHashCode(callSuper = true)
public class InputNewUser extends InputRegisterNewUser {

    private List<Long> roleIds;
}
