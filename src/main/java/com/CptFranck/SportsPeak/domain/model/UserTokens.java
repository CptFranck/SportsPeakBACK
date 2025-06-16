package com.CptFranck.SportsPeak.domain.model;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import lombok.Getter;

@Getter
public class UserTokens extends UserAccessToken {
    private final String refreshToken;

    public UserTokens(UserEntity user, String accessToken, String refreshToken) {
        super(user, accessToken);
        this.refreshToken = refreshToken;
    }
}
