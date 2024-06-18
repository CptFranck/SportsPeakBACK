package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.security.JwtProvider;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.enumType.UserRole;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.user.InputNewUser;
import com.CptFranck.SportsPeak.domain.input.user.InputUser;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.AuthService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@DgsComponent
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtProvider userAuthProvider;
    private final Mapper<UserEntity, UserDto> userMapper;

    public AuthController(AuthenticationManager authenticationManager,
                          AuthService authService,
                          JwtProvider userAuthProvider,
                          Mapper<UserEntity, UserDto> userMapper) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.userAuthProvider = userAuthProvider;
        this.userMapper = userMapper;
    }

    @DgsQuery
    public AuthDto login(@InputArgument InputCredentials inputCredentials) throws Exception {
        UserDto user = userMapper.mapTo(authService.login(inputCredentials));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(inputCredentials.getEmail(), inputCredentials.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new AuthDto(userAuthProvider.generateToken(authentication), user);
    }

    @DgsMutation
    public AuthDto register(@InputArgument InputNewUser inputNewUser) {
        UserDto user = userMapper.mapTo(authService.register(this.inputToEntity(inputNewUser)));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(inputNewUser.getEmail(), inputNewUser.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new AuthDto(userAuthProvider.generateToken(authentication), user);
    }

    private UserEntity inputToEntity(InputNewUser inputNewUser) {
        Long id = null;
        if (inputNewUser instanceof InputUser) {
            id = ((InputUser) inputNewUser).getId();
        }
        return new UserEntity(
                id,
                inputNewUser.getEmail(),
                inputNewUser.getFirstName(),
                inputNewUser.getLastName(),
                inputNewUser.getUsername(),
                inputNewUser.getPassword(),
                UserRole.USER
        );
    }
}
