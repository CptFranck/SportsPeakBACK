package com.CptFranck.SportsPeak.service;


import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.credentials.RegisterInput;
import com.CptFranck.SportsPeak.domain.input.user.InputUserEmail;
import com.CptFranck.SportsPeak.domain.input.user.InputUserPassword;
import com.CptFranck.SportsPeak.domain.model.UserTokens;

public interface AuthService {

    UserTokens login(InputCredentials credentials);

    UserTokens register(RegisterInput registerInput);

    UserTokens updateEmail(InputUserEmail inputUserEmail);

    UserTokens updatePassword(InputUserPassword inputUserPassword);
}