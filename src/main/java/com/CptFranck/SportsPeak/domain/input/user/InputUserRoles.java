package com.CptFranck.SportsPeak.domain.input.user;

import lombok.Data;

import java.util.List;

@Data
public class InputUserRoles {

    private Long id;

    private List<Long> roleIds;
}
