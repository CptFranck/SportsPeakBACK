package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.credentials.RegisterInput;
import com.CptFranck.SportsPeak.domain.input.user.InputUserEmail;
import com.CptFranck.SportsPeak.domain.input.user.InputUserPassword;
import com.CptFranck.SportsPeak.domain.model.UserToken;

public interface AuthService {

    UserToken login(InputCredentials credentials);

    UserToken register(RegisterInput registerInput);

    UserToken updateEmail(InputUserEmail inputUserEmail);

    UserToken updatePassword(InputUserPassword inputUserPassword);
}