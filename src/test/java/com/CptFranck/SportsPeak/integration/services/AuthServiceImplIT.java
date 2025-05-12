package com.CptFranck.SportsPeak.integration.services;

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
import com.CptFranck.SportsPeak.service.serviceImpl.AuthServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.AuthUtils.createInputRegisterNewUser;
import static com.CptFranck.SportsPeak.utils.TestRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class AuthServiceImplIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthServiceImpl authServiceImpl;

    private UserEntity user;
    private String rawPassword;

    @BeforeEach
    void setUp() {
        roleRepository.save(createTestRole(null, 0));
        user = createTestUser(null);
        rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode("password"));
        user = userRepository.save(user);
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void login_EmailNotFound_ThrowEmailUnknownException() {
        InputCredentials wrongEmailInputCredentials =
                new InputCredentials("wrong@email.test", rawPassword);

        assertThrows(EmailUnknownException.class, () -> authServiceImpl.login(wrongEmailInputCredentials));
    }

    @Test
    void login_IncorrectPassword_ThrowIncorrectPasswordException() {
        InputCredentials wrongPasswordInputCredentials =
                new InputCredentials(user.getEmail(), "wrongPassword");

        assertThrows(IncorrectPasswordException.class, () -> authServiceImpl.login(wrongPasswordInputCredentials));
    }

    @Test
    void login_CorrectCredentials_ReturnUserEntity() {
        InputCredentials wrongPasswordInputCredentials =
                new InputCredentials(user.getEmail(), rawPassword);

        UserEntity returnedUser = authServiceImpl.login(wrongPasswordInputCredentials);

        assertEqualsUser(user, returnedUser);
    }

    @Test
    void register_MissingUserRoleInDB_ThrowRoleNotFoundException() {
        UserEntity userToRegister = createTestUserBis(null);
        InputRegisterNewUser inputRegisterNewUser = createInputRegisterNewUser(userToRegister);
        roleRepository.deleteAll();

        Assertions.assertThrows(RoleNotFoundException.class, () -> authServiceImpl.register(inputRegisterNewUser));
    }

    @Test
    void register_UserEmailAlreadyTaken_ThrowEmailAlreadyUsedException() {
        UserEntity userToRegister = createTestUserBis(null);
        InputRegisterNewUser inputRegisterNewUser = createInputRegisterNewUser(userToRegister);
        inputRegisterNewUser.setEmail(user.getEmail());

        Assertions.assertThrows(EmailAlreadyUsedException.class, () -> authServiceImpl.register(inputRegisterNewUser));
    }

    @Test
    void register_UserUsernameAlreadyUsed_ThrowUsernameExistsException() {
        UserEntity userToRegister = createTestUserBis(null);
        userToRegister.setUsername(user.getUsername());
        InputRegisterNewUser inputRegisterNewUser = createInputRegisterNewUser(userToRegister);

        Assertions.assertThrows(UsernameExistsException.class, () -> authServiceImpl.register(inputRegisterNewUser));
    }

    @Test
    void register_CorrectCredentials_ReturnUserEntity() {
        UserEntity userToRegister = createTestUserBis(user.getId() + 1);
        InputRegisterNewUser inputRegisterNewUser = createInputRegisterNewUser(userToRegister);

        UserEntity registeredUser = authServiceImpl.register(inputRegisterNewUser);

        Assertions.assertEquals(userToRegister.getId(), registeredUser.getId());
        Assertions.assertEquals(userToRegister.getEmail(), registeredUser.getEmail());
        Assertions.assertEquals(userToRegister.getFirstName(), registeredUser.getFirstName());
        Assertions.assertEquals(userToRegister.getLastName(), registeredUser.getLastName());
        Assertions.assertEquals(userToRegister.getUsername(), registeredUser.getUsername());
        Assertions.assertTrue(passwordEncoder.matches(userToRegister.getPassword(), registeredUser.getPassword()));

    }
}
