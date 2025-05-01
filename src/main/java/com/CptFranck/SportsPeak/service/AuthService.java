package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.user.InputRegisterNewUser;

public interface AuthService {

    UserEntity login(InputCredentials credentials);

    UserEntity register(InputRegisterNewUser inputRegisterNewUser);

}