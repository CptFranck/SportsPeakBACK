package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.config.security.JwtProvider;
import com.CptFranck.SportsPeak.controller.AuthController;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.EmailAlreadyUsedException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.EmailUnknownException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.IncorrectPasswordException;
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
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.AuthUtils.createInputRegisterNewUser;
import static com.CptFranck.SportsPeak.utils.TestRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUserBis;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class AuthControllerIntTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtProvider jwtProvider;

    private UserEntity user;
    private String rawPassword;

    @BeforeEach
    void init() {
        roleRepository.save(createTestRole(null, 0));
        user = createTestUser(null);
        rawPassword = user.getPassword();
        user = authService.register(createInputRegisterNewUser(user));
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void login_EmailNotFound_ThrowEmailUnknownException() {
        InputCredentials inputCredentials = new InputCredentials("user.getEmail()", rawPassword);

        Assertions.assertThrows(EmailUnknownException.class, () -> authController.login(inputCredentials));
    }

    @Test
    public void login_IncorrectPassword_ThrowIncorrectPasswordException() {
        InputCredentials inputCredentials = new InputCredentials(user.getEmail(), "rawPassword");

        Assertions.assertThrows(IncorrectPasswordException.class, () -> authController.login(inputCredentials));
    }

    @Test
    public void login_CorrectCredentials_ReturnAuthDto() {
        InputCredentials inputCredentials = new InputCredentials(user.getEmail(), rawPassword);

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
        Assertions.assertTrue(jwtProvider.validateToken(authDto.getAccessToken()));

        Assertions.assertEquals(userEntity.getEmail(), authDto.getUser().getEmail());
        Assertions.assertEquals(userEntity.getFirstName(), authDto.getUser().getFirstName());
        Assertions.assertEquals(userEntity.getLastName(), authDto.getUser().getLastName());
        Assertions.assertEquals(userEntity.getUsername(), authDto.getUser().getUsername());
        if (beenRegistered)
            Assertions.assertEquals(userEntity.getRoles().size(), authDto.getUser().getRoles().size());
    }
}
