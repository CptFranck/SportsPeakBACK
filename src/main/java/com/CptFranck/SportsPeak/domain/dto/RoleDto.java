package com.CptFranck.SportsPeak.domain.dto;

import lombok.*;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private Long id;

    private String name;

    private Collection<UserDto> users;

    @EqualsAndHashCode.Exclude
    private Collection<PrivilegeDto> privileges;

}
