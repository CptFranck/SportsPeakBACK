package com.CptFranck.SportsPeak.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class JWToken {
    String token;
    LocalDateTime expiration;
}
