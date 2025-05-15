package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.user.InputRegisterNewUser;
import com.CptFranck.SportsPeak.domain.model.UserToken;

public interface AuthService {

    UserToken login(InputCredentials credentials);

    UserToken register(InputRegisterNewUser inputRegisterNewUser);

}