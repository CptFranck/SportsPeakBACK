package com.CptFranck.SportsPeak.domain.dto;

import lombok.Data;

@Data
public class AuthDto {
    private String accessToken;
    private String tokenType = "Bearer";
    private UserDto user;

    public AuthDto(String accessToken, UserDto user) {
        this.user = user;
        this.accessToken = accessToken;
    }
}
