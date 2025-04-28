package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.config.security.JwtProvider;
import com.CptFranck.SportsPeak.controller.AuthController;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
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

import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUserBis;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
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
    void setUp() {
        user = createTestUser(null);
        rawPassword = user.getPassword();
        user = authService.register(user);
        roleRepository.save(createTestRole(null, 0));
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void AuthController_Login_UnsuccessfulEmailUnknown() {
        InputCredentials inputCredentials = new InputCredentials("user.getEmail()", rawPassword);

        Assertions.assertThrows(EmailUnknownException.class,
                () -> authController.login(inputCredentials));
    }

    @Test
    public void AuthController_Login_UnsuccessfulIncorrectPass() {
        InputCredentials inputCredentials = new InputCredentials(user.getEmail(), "rawPassword");

        Assertions.assertThrows(IncorrectPasswordException.class,
                () -> authController.login(inputCredentials));
    }

    @Test
    public void AuthController_Login_ReturnAuthDto() {
        InputCredentials inputCredentials = new InputCredentials(user.getEmail(), rawPassword);

        AuthDto authDto = authController.login(inputCredentials);

        assertAuthDto(authDto, user, true);
    }

    @Test
    public void AuthController_Register_UnsuccessfulEmailAlreadyUsed() {
        InputRegisterNewUser inputCredentials = new InputRegisterNewUser(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getPassword()
        );

        Assertions.assertThrows(EmailAlreadyUsedException.class,
                () -> authController.register(inputCredentials));
    }

    @Test
    public void AuthController_Register_UnsuccessfulUsernameExists() {
        UserEntity userBis = createTestUserBis(null);

        InputRegisterNewUser inputCredentials = new InputRegisterNewUser(
                userBis.getEmail(),
                userBis.getFirstName(),
                userBis.getLastName(),
                user.getUsername(),
                userBis.getPassword()
        );

        Assertions.assertThrows(UsernameExistsException.class,
                () -> authController.register(inputCredentials));
    }

    @Test
    public void AuthController_Register_ReturnAuthDto() {
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
