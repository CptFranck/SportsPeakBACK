package com.CptFranck.SportsPeak.service.serviceImpl;

import com.CptFranck.SportsPeak.config.security.JwtProvider;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.IncorrectPasswordException;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.user.InputRegisterNewUser;
import com.CptFranck.SportsPeak.domain.input.user.InputUserEmail;
import com.CptFranck.SportsPeak.domain.input.user.InputUserPassword;
import com.CptFranck.SportsPeak.domain.model.JWToken;
import com.CptFranck.SportsPeak.domain.model.UserToken;
import com.CptFranck.SportsPeak.service.AuthService;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final RoleService roleService;

    private final UserService userService;

    private final JwtProvider userAuthProvider;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(RoleService roleService, UserService userService, JwtProvider userAuthProvider, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userAuthProvider = userAuthProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserToken login(InputCredentials credentials) {
        String email = credentials.getEmail();
        String password = credentials.getPassword();
        UserEntity user = userService.findByEmail(email);

        verifyPassword(password, user);

        return getUserToken(email, password, user);
    }

    @Override
    public UserToken register(InputRegisterNewUser inputRegisterNewUser) {
        RoleEntity userRole = roleService.findByName("ROLE_USER").orElseThrow(() -> new RoleNotFoundException("ROLE_USER"));

        String email = inputRegisterNewUser.getEmail();
        String password = inputRegisterNewUser.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        UserEntity user = new UserEntity(
                null,
                email,
                inputRegisterNewUser.getFirstName(),
                inputRegisterNewUser.getLastName(),
                inputRegisterNewUser.getUsername(),
                encodedPassword,
                Set.of(userRole),
                new HashSet<>(),
                new HashSet<>()
        );

        UserEntity userSaved = userService.save(user);

        return getUserToken(email, password, userSaved);
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

        return getUserToken(newEmail, password, userUpdated);
    }

    @Override
    public UserToken updatePassword(InputUserPassword inputUserPassword) {
        Long id = inputUserPassword.getId();
        String oldPassword = inputUserPassword.getOldPassword();
        String newPassword = inputUserPassword.getNewPassword();

        UserEntity user = userService.findOne(id);
        verifyPassword(oldPassword, user);

        user.setPassword(passwordEncoder.encode(newPassword));
        UserEntity userSaved = userService.save(user);

        return getUserToken(user.getEmail(), newPassword, userSaved);
    }

    private void verifyPassword(String password, UserEntity user) {
        boolean match = passwordEncoder.matches(password, user.getPassword());
        if (!match) throw new IncorrectPasswordException();
    }

    private UserToken getUserToken(String login, String password, UserEntity user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JWToken token = userAuthProvider.generateToken(authentication);
        return new UserToken(token, user);
    }
}
