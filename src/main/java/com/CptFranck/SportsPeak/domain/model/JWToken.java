package com.CptFranck.SportsPeak.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JWToken {
    String token;
    LocalDateTime expiration;
}
