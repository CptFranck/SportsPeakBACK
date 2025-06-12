package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.credentials.RegisterInput;
import com.CptFranck.SportsPeak.domain.model.UserTokens;
import com.CptFranck.SportsPeak.mapper.Mapper;
import com.CptFranck.SportsPeak.service.AuthService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;

@DgsComponent
public class AuthController {

    private final AuthService authService;

    private final Mapper<UserEntity, UserDto> userMapper;

    public AuthController(AuthService authService, Mapper<UserEntity, UserDto> userMapper) {
        this.userMapper = userMapper;
        this.authService = authService;
    }

    @DgsMutation
    public AuthDto login(@InputArgument InputCredentials inputCredentials
//            , DataFetchingEnvironment dfe
    ) {
        UserTokens userToken = authService.login(inputCredentials);

//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if (attributes != null) {
//            HttpServletResponse response = attributes.getResponse();
//            if (response != null) {
//                Cookie refreshTokenCookie = new Cookie("refreshToken", userToken.getRefreshToken());
//                refreshTokenCookie.setHttpOnly(true);
//                refreshTokenCookie.setSecure(false); // mettre à false en dev HTTP
//                refreshTokenCookie.setPath("/");
//                refreshTokenCookie.setMaxAge(Duration.ofDays(7).toMillisPart()); // 7 jours
//                response.addCookie(refreshTokenCookie);
//            }
//        }

        return new AuthDto(userToken.getAccessToken(), userMapper.mapTo(userToken.getUser()));
    }

    @DgsMutation
    public AuthDto register(@InputArgument RegisterInput registerInput) {
        UserTokens userToken = authService.register(registerInput);

//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if (attributes != null) {
//            HttpServletResponse response = attributes.getResponse();
//            if (response != null) {
//                Cookie refreshTokenCookie = new Cookie("refreshToken", userToken.getRefreshToken());
//                refreshTokenCookie.setHttpOnly(true);
//                refreshTokenCookie.setSecure(true); // mettre à false en dev HTTP
//                refreshTokenCookie.setPath("/");
//                refreshTokenCookie.setMaxAge(7 * 24 * 3600); // 7 jours
//                response.addCookie(refreshTokenCookie);
//            }
//        }

        return new AuthDto(userToken.getAccessToken(), userMapper.mapTo(userToken.getUser()));
    }

//    @DgsMutation
//    public AuthDto refreshToken() {
//        String oldRefreshToken = extractFromCookie(); // depuis HttpServletRequest
//        UserTokens tokens = authService.refreshTokens(oldRefreshToken);
//        cookieService.setRefreshTokenCookie(tokens.getRefreshToken());
//        return new AuthDto(tokens.getAccessToken(), ...);
//    }
}
