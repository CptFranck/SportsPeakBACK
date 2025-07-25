package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.config.security.jwt.JwtUtils;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.TokenEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.enumType.TokenType;
import com.CptFranck.SportsPeak.domain.exception.token.InvalidTokenException;
import com.CptFranck.SportsPeak.domain.exception.token.RefreshTokenExpiredException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.InvalidCredentialsException;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.credentials.RegisterInput;
import com.CptFranck.SportsPeak.domain.input.user.InputUserEmail;
import com.CptFranck.SportsPeak.domain.input.user.InputUserPassword;
import com.CptFranck.SportsPeak.domain.model.UserTokens;
import com.CptFranck.SportsPeak.service.AuthService;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.TokenService;
import com.CptFranck.SportsPeak.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUtils jwtUtils;

    private final RoleService roleService;

    private final UserService userService;

    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(JwtUtils jwtUtils,
                           RoleService roleService,
                           UserService userService,
                           TokenService tokenService,
                           PasswordEncoder passwordEncoder,
                           UserDetailsService userDetailsService,
                           AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.roleService = roleService;
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserTokens register(RegisterInput registerInput) {
        RoleEntity userRole = roleService.findByName("USER");

        String email = registerInput.getEmail();
        String password = registerInput.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        UserEntity user = new UserEntity(
                email,
                registerInput.getFirstName(),
                registerInput.getLastName(),
                registerInput.getUsername(),
                encodedPassword
        );
        user.getRoles().add(userRole);

        UserEntity userSaved = userService.save(user);
        authenticate(email, password);

        tokenService.revokeAllUserTokens(userSaved);
        return generateTokens(userSaved);
    }

    @Override
    public UserTokens login(InputCredentials credentials) {
        String login = credentials.getLogin();
        String password = credentials.getPassword();

        authenticate(login, password);
        UserEntity user = userService.findByLogin(login);

        tokenService.revokeAllUserTokens(user);
        return generateTokens(user);
    }

    @Override
    public UserTokens refreshAccessToken(String refreshToken) {
        if (!jwtUtils.validateToken(refreshToken))
            throw new InvalidTokenException("Invalid refresh token");

        if (!tokenService.isTokenValidInStore(refreshToken))
            throw new RefreshTokenExpiredException("Refresh token revoked or expired");

        final String username = jwtUtils.extractUsername(refreshToken);
        if (username == null) throw new InvalidTokenException("Username token missing");

        UserEntity user = userService.findByLogin(username);

        tokenService.revokeAllUserTokens(user);
        return generateTokens(user);
    }

    @Override
    public UserTokens updateEmail(InputUserEmail inputUserEmail) {
        Long id = inputUserEmail.getId();
        String password = inputUserEmail.getPassword();
        String newEmail = inputUserEmail.getNewEmail();

        UserEntity user = userService.findOne(id);
        authenticate(user.getEmail(), password);

        user.setEmail(newEmail);
        UserEntity userUpdated = userService.save(user);

        tokenService.revokeAllUserTokens(userUpdated);
        return generateTokens(userUpdated);
    }

    @Override
    public UserTokens updatePassword(InputUserPassword inputUserPassword) {
        UserEntity user = userService.findOne(inputUserPassword.getId());

        String email = user.getEmail();
        String oldPassword = inputUserPassword.getOldPassword();
        String newPassword = inputUserPassword.getNewPassword();

        authenticate(email, oldPassword);

        user.setPassword(passwordEncoder.encode(newPassword));
        UserEntity userUpdated = userService.save(user);

        tokenService.revokeAllUserTokens(userUpdated);
        return generateTokens(userUpdated);
    }

    private void authenticate(String login, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException(e);
        }
    }

    private UserTokens generateTokens(UserEntity user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String accessToken = createAndStoreToken(userDetails, user, TokenType.ACCESS);
        String refreshToken = createAndStoreToken(userDetails, user, TokenType.REFRESH);

        return new UserTokens(user, accessToken, refreshToken);
    }

    private String createAndStoreToken(UserDetails userDetails, UserEntity user, TokenType tokenType) {
        String token;
        if (tokenType.equals(TokenType.REFRESH))
            token = jwtUtils.generateRefreshToken(userDetails);
        else
            token = jwtUtils.generateAccessToken(userDetails);

        TokenEntity tokenEntity = new TokenEntity(
                tokenService.hashToken(token),
                tokenType,
                jwtUtils.extractExpiration(token).toInstant(),
                user);
        tokenService.save(tokenEntity);

        return token;
    }
}
