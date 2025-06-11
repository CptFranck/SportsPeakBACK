package com.CptFranck.SportsPeak.domain.dto;

import lombok.Getter;

@Getter
public class AuthDto {
    private final String accessToken;
    private final String tokenType = "Bearer";
    private final UserDto user;

    public AuthDto(String accessToken, UserDto user) {
        this.accessToken = accessToken;
        this.user = user;
    }
}
