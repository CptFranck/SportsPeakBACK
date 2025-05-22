package com.CptFranck.SportsPeak.integration.services.services;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.*;
import com.CptFranck.SportsPeak.domain.input.credentials.InputCredentials;
import com.CptFranck.SportsPeak.domain.input.credentials.RegisterInput;
import com.CptFranck.SportsPeak.domain.input.user.InputUserEmail;
import com.CptFranck.SportsPeak.domain.input.user.InputUserPassword;
import com.CptFranck.SportsPeak.domain.model.UserToken;
import com.CptFranck.SportsPeak.repository.RoleRepository;
import com.CptFranck.SportsPeak.repository.UserRepository;
import com.CptFranck.SportsPeak.service.serviceImpl.AuthServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.AuthTestUtils.createRegisterInput;
import static com.CptFranck.SportsPeak.utils.RoleTestUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.*;
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
    void login_InvalidUsername_ThrowInvalidCredentialsException() {
        InputCredentials wrongUsernameInputCredentials = new InputCredentials("wrongUsername", rawPassword);

        assertThrows(InvalidCredentialsException.class, () -> authServiceImpl.login(wrongUsernameInputCredentials));
    }

    @Test
    void login_InvalidEmail_ThrowInvalidCredentialsException() {
        InputCredentials wrongEmailInputCredentials = new InputCredentials("wrong@email.test", rawPassword);

        assertThrows(InvalidCredentialsException.class, () -> authServiceImpl.login(wrongEmailInputCredentials));
    }

    @Test
    void login_IncorrectPassword_ThrowInvalidCredentialsException() {
        InputCredentials wrongPasswordInputCredentials = new InputCredentials(user.getEmail(), "wrongPassword");

        assertThrows(InvalidCredentialsException.class, () -> authServiceImpl.login(wrongPasswordInputCredentials));
    }

    @Test
    void login_UserDeleted_ThrowInvalidCredentialsException() {
        InputCredentials inputCredentials = new InputCredentials(user.getEmail(), rawPassword);
        userRepository.delete(user);

        assertThrows(InvalidCredentialsException.class, () -> authServiceImpl.login(inputCredentials));
    }

    @Test
    void login_CorrectCredentialsWithEmails_ReturnUserEntity() {
        InputCredentials inputCredentials = new InputCredentials(user.getEmail(), rawPassword);

        UserToken returnedUserToken = authServiceImpl.login(inputCredentials);

        assertEqualsUser(user, returnedUserToken.getUser(), false);
    }

    @Test
    void login_CorrectCredentialsWithUsername_ReturnUserEntity() {
        InputCredentials inputCredentials = new InputCredentials(user.getUsername(), rawPassword);

        UserToken returnedUserToken = authServiceImpl.login(inputCredentials);

        assertEqualsUser(user, returnedUserToken.getUser(), false);
    }

    @Test
    void register_MissingUserRoleInDB_ThrowRoleNotFoundException() {
        UserEntity userToRegister = createTestUserBis(null);
        RegisterInput registerInput = createRegisterInput(userToRegister);
        roleRepository.deleteAll();

        Assertions.assertThrows(RoleNotFoundException.class, () -> authServiceImpl.register(registerInput));
    }

    @Test
    void register_UserEmailAlreadyTaken_ThrowEmailAlreadyUsedException() {
        UserEntity userToRegister = createTestUserBis(null);
        RegisterInput inputRegister = createRegisterInput(userToRegister);
        inputRegister.setEmail(user.getEmail());

        Assertions.assertThrows(EmailAlreadyUsedException.class, () -> authServiceImpl.register(inputRegister));
    }

    @Test
    void register_UserUsernameAlreadyUsed_ThrowUsernameExistsException() {
        UserEntity userToRegister = createTestUserBis(null);
        userToRegister.setUsername(user.getUsername());
        RegisterInput registerInput = createRegisterInput(userToRegister);

        Assertions.assertThrows(UsernameExistsException.class, () -> authServiceImpl.register(registerInput));
    }

    @Test
    void register_ValidRegisterInput_ReturnUserToken() {
        UserEntity userToRegister = createTestUserBis(null);
        RegisterInput registerInput = createRegisterInput(userToRegister);

        UserToken returnedUserToken = authServiceImpl.register(registerInput);

        UserEntity user = returnedUserToken.getUser();
        Assertions.assertEquals(userToRegister.getEmail(), user.getEmail());
        Assertions.assertEquals(userToRegister.getFirstName(), user.getFirstName());
        Assertions.assertEquals(userToRegister.getLastName(), user.getLastName());
        Assertions.assertEquals(userToRegister.getUsername(), user.getUsername());
        Assertions.assertTrue(passwordEncoder.matches(userToRegister.getPassword(), user.getPassword()));
    }

    @Test
    void updateEmail_InvalidUserId_ThrowUserNotFoundException() {
        InputUserEmail inputUserEmail = createTestInputUserEmail(user.getId(), rawPassword);
        userRepository.delete(user);

        Assertions.assertThrows(UserNotFoundException.class, () -> authServiceImpl.updateEmail(inputUserEmail));
    }

    @Test
    void updateEmail_IncorrectPassword_ThrowUserNotFoundException() {
        InputUserEmail inputUserEmail = createTestInputUserEmail(user.getId(), "wrongPassword");

        Assertions.assertThrows(IncorrectPasswordException.class, () -> authServiceImpl.updateEmail(inputUserEmail));
    }

    @Test
    void updateEmail_UserEmailAlreadyUsed_ThrowUsernameExistsException() {
        UserEntity userToRegister = userRepository.save(createTestUserBis(null));
        InputUserEmail inputUserEmail = createTestInputUserEmail(user.getId(), rawPassword);
        inputUserEmail.setNewEmail(userToRegister.getEmail());

        Assertions.assertThrows(EmailAlreadyUsedException.class, () -> authServiceImpl.updateEmail(inputUserEmail));
    }

    @Test
    void updateEmail_ValidInput_ReturnUserToken() {
        InputUserEmail inputUserEmail = createTestInputUserEmail(user.getId(), rawPassword);

        UserToken returnedUserToken = authServiceImpl.updateEmail(inputUserEmail);

        UserEntity userSaved = returnedUserToken.getUser();
        Assertions.assertNotEquals(user.getEmail(), userSaved.getEmail());
        Assertions.assertEquals(user.getFirstName(), userSaved.getFirstName());
        Assertions.assertEquals(user.getLastName(), userSaved.getLastName());
        Assertions.assertEquals(user.getUsername(), userSaved.getUsername());
        Assertions.assertEquals(user.getRoles().size(), userSaved.getRoles().size());
        Assertions.assertEquals(user.getSubscribedProgExercises().size(), userSaved.getSubscribedProgExercises().size());
        Assertions.assertEquals(user.getProgExercisesCreated().size(), userSaved.getProgExercisesCreated().size());
    }

    @Test
    void updatePassword_InvalidUserId_ThrowUserNotFoundException() {
        InputUserPassword inputUserPassword = createTestInputUserPassword(user.getId(), rawPassword);
        userRepository.delete(user);

        Assertions.assertThrows(UserNotFoundException.class, () -> authServiceImpl.updatePassword(inputUserPassword));
    }

    @Test
    void updatePassword_InvalidPassword_ThrowIncorrectPasswordException() {
        InputUserPassword inputUserPassword = createTestInputUserPassword(user.getId(), "wrongPassword");

        Assertions.assertThrows(IncorrectPasswordException.class, () -> authServiceImpl.updatePassword(inputUserPassword));
    }

    @Test
    void updatePassword_ValidInput_ReturnUserToken() {
        InputUserPassword inputUserPassword = createTestInputUserPassword(user.getId(), rawPassword);

        UserToken returnedUserToken = authServiceImpl.updatePassword(inputUserPassword);

        UserEntity userSaved = returnedUserToken.getUser();
        Assertions.assertNotEquals(user.getPassword(), userSaved.getPassword());
        Assertions.assertEquals(user.getEmail(), userSaved.getEmail());
        Assertions.assertEquals(user.getFirstName(), userSaved.getFirstName());
        Assertions.assertEquals(user.getLastName(), userSaved.getLastName());
        Assertions.assertEquals(user.getUsername(), userSaved.getUsername());
        Assertions.assertEquals(user.getRoles().size(), userSaved.getRoles().size());
        Assertions.assertEquals(user.getSubscribedProgExercises().size(), userSaved.getSubscribedProgExercises().size());
        Assertions.assertEquals(user.getProgExercisesCreated().size(), userSaved.getProgExercisesCreated().size());
    }
}
