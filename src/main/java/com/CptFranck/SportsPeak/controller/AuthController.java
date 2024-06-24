package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.security.JwtProvider;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.user.InputRegisterNewUser;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.AuthService;
import com.CptFranck.SportsPeak.service.RoleService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@DgsComponent
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final RoleService roleService;
    private final JwtProvider userAuthProvider;
    private final Mapper<UserEntity, UserDto> userMapper;

    public AuthController(AuthenticationManager authenticationManager,
                          AuthService authService,
                          RoleService roleService,
                          JwtProvider userAuthProvider,
                          Mapper<UserEntity, UserDto> userMapper) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.roleService = roleService;
        this.userAuthProvider = userAuthProvider;
        this.userMapper = userMapper;
    }

    @DgsMutation
    public AuthDto login(@InputArgument InputCredentials inputCredentials) throws Exception {
        UserDto user = userMapper.mapTo(authService.login(inputCredentials));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(inputCredentials.getEmail(), inputCredentials.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new AuthDto(userAuthProvider.generateToken(authentication), user);
    }

    @DgsMutation
    public AuthDto register(@InputArgument InputRegisterNewUser inputRegisterNewUser) {
        UserDto user = userMapper.mapTo(authService.register(this.inputToEntity(inputRegisterNewUser)));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(inputRegisterNewUser.getEmail(), inputRegisterNewUser.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new AuthDto(userAuthProvider.generateToken(authentication), user);
    }

    private UserEntity inputToEntity(InputRegisterNewUser inputRegisterNewUser) {
        Optional<RoleEntity> userRole = roleService.findByName("ROLE_USER");
        Collection<RoleEntity> roles = Set.of(userRole.orElseThrow(
                () -> new RuntimeException("ROLE_USER not found or does not exist")));

        return new UserEntity(
                null,
                inputRegisterNewUser.getEmail(),
                inputRegisterNewUser.getFirstName(),
                inputRegisterNewUser.getLastName(),
                inputRegisterNewUser.getUsername(),
                inputRegisterNewUser.getPassword(),
                roles
        );
    }
}
