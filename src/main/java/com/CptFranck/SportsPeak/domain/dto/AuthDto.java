package com.CptFranck.SportsPeak.domain.dto;

import com.CptFranck.SportsPeak.domain.model.JWToken;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthDto {
    private final LocalDateTime expiration;
    private String accessToken;
    private String tokenType = "Bearer";
    private UserDto user;

    public AuthDto(JWToken jwToken, UserDto user) {
        this.user = user;
        this.accessToken = jwToken.getToken();
        this.expiration = jwToken.getExpiration();
    }
}
