package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.config.security.jwt.JwtUtils;
import com.CptFranck.SportsPeak.controller.AuthController;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.EmailAlreadyUsedException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.InvalidCredentialsException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UsernameExistsException;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.credentials.RegisterInput;
import com.CptFranck.SportsPeak.repository.RoleRepository;
import com.CptFranck.SportsPeak.repository.UserRepository;
import com.CptFranck.SportsPeak.service.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.AuthTestUtils.createRegisterInput;
import static com.CptFranck.SportsPeak.utils.RoleTestUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUser;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUserBis;

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
        user = authService.register(createRegisterInput(user)).getUser();
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void login_InvalidLogin_ThrowRuntimeException() {
        InputCredentials inputCredentials = new InputCredentials("login", rawPassword);

        Assertions.assertThrows(InvalidCredentialsException.class, () -> authController.login(inputCredentials));
    }

    @Test
    public void login_IncorrectPassword_ThrowInvalidCredentialsException() {
        InputCredentials inputCredentials = new InputCredentials(user.getEmail(), "rawPassword");

        Assertions.assertThrows(InvalidCredentialsException.class, () -> authController.login(inputCredentials));
    }

    @Test
    public void login_UserDeleted_ThrowInvalidCredentialsException() {
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
    public void login_CorrectCredentialsWithEmail_ReturnAuthDto() {
        InputCredentials inputCredentials = new InputCredentials(user.getEmail(), rawPassword);

        AuthDto authDto = authController.login(inputCredentials);

        assertAuthDto(authDto, user, true);
    }

    @Test
    public void register_MissingUserRoleInDB_ThrowRoleNotFoundException() {
        RegisterInput registerInput = createRegisterInput(user);
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Assertions.assertThrows(RoleNotFoundException.class, () -> authController.register(registerInput));
    }

    @Test
    public void register_UserEmailAlreadyTaken_ThrowEmailAlreadyUsedException() {
        RegisterInput registerInput = createRegisterInput(user);

        Assertions.assertThrows(EmailAlreadyUsedException.class, () -> authController.register(registerInput));
    }

    @Test
    public void register_UserUsernameAlreadyUsed_ThrowUsernameExistsException() {
        UserEntity userBis = createTestUserBis(null);
        RegisterInput registerInput = createRegisterInput(userBis);
        registerInput.setUsername(user.getUsername());

        Assertions.assertThrows(UsernameExistsException.class, () -> authController.register(registerInput));
    }

    @Test
    public void register_ValidRegisterInput_ReturnAuthDto() {
        UserEntity userBis = createTestUserBis(null);
        RegisterInput registerInput = createRegisterInput(userBis);

        AuthDto authDto = authController.register(registerInput);

        assertAuthDto(authDto, userBis, false);
    }

    private void assertAuthDto(AuthDto authDto, UserEntity userEntity, boolean beenRegistered) {
        Assertions.assertNotNull(authDto);
        Assertions.assertEquals("Bearer", authDto.getTokenType());
        Assertions.assertTrue(jwtProvider.validateToken(authDto.getAccessToken()));

        Assertions.assertEquals(userEntity.getEmail(), authDto.getUser().getEmail());
        Assertions.assertEquals(userEntity.getFirstName(), authDto.getUser().getFirstName());
        Assertions.assertEquals(userEntity.getLastName(), authDto.getUser().getLastName());
        Assertions.assertEquals(userEntity.getUsername(), authDto.getUser().getUsername());
        if (beenRegistered)
            Assertions.assertEquals(userEntity.getRoles().size(), authDto.getUser().getRoles().size());
    }
}
