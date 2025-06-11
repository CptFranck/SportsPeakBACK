package com.CptFranck.SportsPeak.domain.model;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserTokens {
    private final UserEntity user;
    private final String accessToken;
    private final String refreshToken;
}
