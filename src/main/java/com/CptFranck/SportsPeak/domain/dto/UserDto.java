package com.CptFranck.SportsPeak.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String token;
}
