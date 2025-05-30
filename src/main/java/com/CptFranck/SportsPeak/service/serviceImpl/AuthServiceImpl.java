package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.config.security.JwtUtils;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.userAuth.IncorrectPasswordException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.InvalidCredentialsException;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.credentials.RegisterInput;
import com.CptFranck.SportsPeak.domain.input.user.InputUserEmail;
import com.CptFranck.SportsPeak.domain.input.user.InputUserPassword;
import com.CptFranck.SportsPeak.domain.model.UserToken;
import com.CptFranck.SportsPeak.service.AuthService;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUtils jwtUtils;

    private final RoleService roleService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(JwtUtils jwtUtils, RoleService roleService, UserService userService, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserToken login(InputCredentials credentials) {
        String login = credentials.getLogin();
        String password = credentials.getPassword();

        String token = authenticate(login, password);
        UserEntity user = userService.findByLogin(login);

        return new UserToken(token, user);
    }

    @Override
    public UserToken register(RegisterInput registerInput) {
        RoleEntity userRole = roleService.findByName("USER");

        String email = registerInput.getEmail();
        String password = registerInput.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        UserEntity user = new UserEntity(
                null,
                email,
                registerInput.getFirstName(),
                registerInput.getLastName(),
                registerInput.getUsername(),
                encodedPassword,
                Set.of(userRole),
                new HashSet<>(),
                new HashSet<>()
        );

        UserEntity userSaved = userService.save(user);
        String token = authenticate(email, password);

        return new UserToken(token, userSaved);
    }

    @Override
    public UserToken updateEmail(InputUserEmail inputUserEmail) {
        Long id = inputUserEmail.getId();
        String password = inputUserEmail.getPassword();
        String newEmail = inputUserEmail.getNewEmail();

        UserEntity user = userService.findOne(id);
        verifyPassword(password, user);

        user.setEmail(newEmail);
        UserEntity userUpdated = userService.save(user);
        String token = authenticate(userUpdated.getEmail(), password);

        return new UserToken(token, userUpdated);
    }

    @Override
    public UserToken updatePassword(InputUserPassword inputUserPassword) {
        UserEntity user = userService.findOne(inputUserPassword.getId());

        String email = user.getEmail();
        String oldPassword = inputUserPassword.getOldPassword();
        String newPassword = inputUserPassword.getNewPassword();

        verifyPassword(oldPassword, user);

        user.setPassword(passwordEncoder.encode(newPassword));
        UserEntity userUpdated = userService.save(user);
        String token = authenticate(email, newPassword);

        return new UserToken(token, userUpdated);
    }

    private String authenticate(String login, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException(e);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        return jwtUtils.generateToken(userDetails);
    }

    private void verifyPassword(String password, UserEntity user) {
        boolean match = passwordEncoder.matches(password, user.getPassword());
        if (!match) throw new IncorrectPasswordException();
    }
}
