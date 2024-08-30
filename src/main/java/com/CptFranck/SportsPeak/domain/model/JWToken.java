package com.CptFranck.SportsPeak.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWToken {
    String token;
    LocalDateTime expiration;
}
