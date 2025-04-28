package com.CptFranck.SportsPeak.integration.services;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.userAuth.EmailAlreadyUsedException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.EmailUnknownException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.IncorrectPasswordException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UsernameExistsException;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import com.CptFranck.SportsPeak.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class AuthServiceImplIntTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthServiceImpl authServiceImpl;

    private UserEntity user;
    private String rawPassword;

    @BeforeEach
    void setUp() {
        user = createTestUser(null);
        rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode("password"));
        user = userRepository.save(user);
    }

    @AfterEach
    public void afterEach() {
        this.userRepository.deleteAll();
    }

    @Test
    void authService_Login_UnsuccessfulEmailUnknown() {
        InputCredentials wrongEmailInputCredentials =
                new InputCredentials("wrong@email.test", rawPassword);

        assertThrows(EmailUnknownException.class, () -> authServiceImpl.login(wrongEmailInputCredentials));
    }

    @Test
    void authService_Login_UnsuccessfulIncorrectPassword() {
        InputCredentials wrongPasswordInputCredentials =
                new InputCredentials(user.getEmail(), "wrongPassword");

        assertThrows(IncorrectPasswordException.class, () -> authServiceImpl.login(wrongPasswordInputCredentials));
    }

    @Test
    void authService_Login_Successful() {
        InputCredentials wrongPasswordInputCredentials =
                new InputCredentials(user.getEmail(), rawPassword);

        UserEntity returnedUser = authServiceImpl.login(wrongPasswordInputCredentials);

        assertEqualsUser(user, returnedUser);
    }

    @Test
    void authService_Register_UnsuccessfulEmailAlreadyUsed() {
        UserEntity userToRegister = createTestUserBis(null);
        userToRegister.setEmail(user.getEmail());

        Assertions.assertThrows(EmailAlreadyUsedException.class, () -> authServiceImpl.register(userToRegister));
    }

    @Test
    void authService_Register_UnsuccessfulUsernameAlreadyUsed() {
        UserEntity userToRegister = createTestUserBis(null);
        userToRegister.setUsername(user.getUsername());

        Assertions.assertThrows(UsernameExistsException.class, () -> authServiceImpl.register(userToRegister));
    }

    @Test
    void authService_Register_Success() {
        UserEntity userToRegister = createTestUserBis(null);
        userToRegister.setPassword(passwordEncoder.encode("password"));

        UserEntity registeredUser = authServiceImpl.register(userToRegister);

        assertEqualsUser(userToRegister, registeredUser);
    }
}
