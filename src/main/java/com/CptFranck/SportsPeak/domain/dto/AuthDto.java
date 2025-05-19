package com.CptFranck.SportsPeak.domain.dto;

import lombok.Getter;

@Getter
public class AuthDto {
    private final String token;
    private final String tokenType = "Bearer";
    private final UserDto user;

    public AuthDto(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }
}
