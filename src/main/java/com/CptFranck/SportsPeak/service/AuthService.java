package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;

public interface AuthService {

    UserEntity login(InputCredentials credentials) throws Exception;

    UserEntity register(UserEntity user);

}