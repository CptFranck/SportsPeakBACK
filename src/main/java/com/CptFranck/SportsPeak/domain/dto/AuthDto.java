package com.CptFranck.SportsPeak.domain.dto;

import com.CptFranck.SportsPeak.domain.model.JWToken;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuthDto {
    private final LocalDateTime expiration;
    private final String accessToken;
    private final String tokenType = "Bearer";
    private final UserDto user;

    public AuthDto(JWToken jwToken, UserDto user) {
        this.user = user;
        this.accessToken = jwToken.getToken();
        this.expiration = jwToken.getExpiration();
    }
}
