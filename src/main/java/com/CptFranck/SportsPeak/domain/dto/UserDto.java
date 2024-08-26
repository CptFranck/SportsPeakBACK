package com.CptFranck.SportsPeak.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String username;

    private Set<RoleDto> roles;

    private Set<ProgExerciseDto> subscribedProgExercises;

    private Set<ProgExerciseDto> progExercisesCreated;
}
