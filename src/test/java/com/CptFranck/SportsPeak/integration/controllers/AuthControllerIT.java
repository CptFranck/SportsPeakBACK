package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.config.security.JwtUtils;
import com.CptFranck.SportsPeak.controller.AuthController;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.EmailAlreadyUsedException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.InvalidCredentialsException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UsernameExistsException;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.user.InputRegisterNewUser;
import com.CptFranck.SportsPeak.repositories.RoleRepository;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import com.CptFranck.SportsPeak.service.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.AuthUtils.createInputRegisterNewUser;
import static com.CptFranck.SportsPeak.utils.TestRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUserBis;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class AuthControllerIT {

    @Autowired
    private AuthController authController;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtProvider;

    private UserEntity user;
    private String rawPassword;

    @BeforeEach
    void init() {
        roleRepository.save(createTestRole(null, 0));
        user = createTestUser(null);
        rawPassword = user.getPassword();
        user = authService.register(createInputRegisterNewUser(user)).getUser();
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void login_InvalidLogin_ThrowsRuntimeException() {
        InputCredentials inputCredentials = new InputCredentials("login", rawPassword);

        Assertions.assertThrows(RuntimeException.class, () -> authController.login(inputCredentials));
    }

    @Test
    public void login_IncorrectPassword_ThrowsInvalidCredentialsException() {
        InputCredentials inputCredentials = new InputCredentials(user.getEmail(), "rawPassword");

        Assertions.assertThrows(InvalidCredentialsException.class, () -> authController.login(inputCredentials));
    }

    @Test
    public void login_CorrectCredentialsWithEmail_ReturnAuthDto() {
        InputCredentials inputCredentials = new InputCredentials(user.getEmail(), rawPassword);

        AuthDto authDto = authController.login(inputCredentials);

        assertAuthDto(authDto, user, true);
    }

    @Test
    public void login_UserDeleted_ThrowsInvalidCredentialsException() {
        InputCredentials inputCredentials = new InputCredentials(user.getEmail(), rawPassword);
        userRepository.delete(user);

        Assertions.assertThrows(InvalidCredentialsException.class, () -> authController.login(inputCredentials));
    }

    @Test
    public void login_CorrectCredentialsWithUsername_ReturnAuthDto() {
        InputCredentials inputCredentials = new InputCredentials(user.getUsername(), rawPassword);

        AuthDto authDto = authController.login(inputCredentials);

        assertAuthDto(authDto, user, true);
    }

    @Test
    public void register_MissingUserRoleInDB_ThrowRoleNotFoundException() {
        InputRegisterNewUser inputCredentials = createInputRegisterNewUser(user);
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Assertions.assertThrows(RoleNotFoundException.class, () -> authController.register(inputCredentials));
    }

    @Test
    public void register_UserEmailAlreadyTaken_ThrowEmailAlreadyUsedException() {
        InputRegisterNewUser inputCredentials = createInputRegisterNewUser(user);

        Assertions.assertThrows(EmailAlreadyUsedException.class, () -> authController.register(inputCredentials));
    }

    @Test
    public void register_UserUsernameAlreadyUsed_ThrowUsernameExistsException() {
        UserEntity userBis = createTestUserBis(null);
        InputRegisterNewUser inputCredentials = createInputRegisterNewUser(userBis);
        inputCredentials.setUsername(user.getUsername());

        Assertions.assertThrows(UsernameExistsException.class, () -> authController.register(inputCredentials));
    }

    @Test
    public void register_CorrectCredentials_ReturnAuthDto() {
        UserEntity userBis = createTestUserBis(null);

        InputRegisterNewUser inputCredentials = new InputRegisterNewUser(
                userBis.getEmail(),
                userBis.getFirstName(),
                userBis.getLastName(),
                userBis.getUsername(),
                userBis.getPassword()
        );

        AuthDto authDto = authController.register(inputCredentials);

        assertAuthDto(authDto, userBis, false);
    }

    private void assertAuthDto(AuthDto authDto, UserEntity userEntity, boolean beenRegistered) {
        Assertions.assertNotNull(authDto);
        Assertions.assertEquals("Bearer", authDto.getTokenType());
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEntity.getEmail());
        Assertions.assertTrue(jwtProvider.validateToken(authDto.getToken(), userDetails));

        Assertions.assertEquals(userEntity.getEmail(), authDto.getUser().getEmail());
        Assertions.assertEquals(userEntity.getFirstName(), authDto.getUser().getFirstName());
        Assertions.assertEquals(userEntity.getLastName(), authDto.getUser().getLastName());
        Assertions.assertEquals(userEntity.getUsername(), authDto.getUser().getUsername());
        if (beenRegistered)
            Assertions.assertEquals(userEntity.getRoles().size(), authDto.getUser().getRoles().size());
    }
}
