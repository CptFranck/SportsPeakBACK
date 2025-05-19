package com.CptFranck.SportsPeak.domain.model;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserToken {
    String token;
    UserEntity user;
}
