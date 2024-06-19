package com.CptFranck.SportsPeak.domain.dto;

import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String username;

    @EqualsAndHashCode.Exclude
    private Set<RoleDto> roles;
}
