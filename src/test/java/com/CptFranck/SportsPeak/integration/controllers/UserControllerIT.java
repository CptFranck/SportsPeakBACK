package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.controller.UserController;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.userAuth.EmailAlreadyUsedException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UserNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UsernameExistsException;
import com.CptFranck.SportsPeak.domain.input.user.*;
import com.CptFranck.SportsPeak.repository.ExerciseRepository;
import com.CptFranck.SportsPeak.repository.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repository.RoleRepository;
import com.CptFranck.SportsPeak.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.assertProgExerciseDtoAndEntity;
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.RoleTestUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class UserControllerIT {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserEntity user;
    private UserEntity userBis;
    private String rawPassword;

    @BeforeEach
    void setUp() {
        user = createTestUser(null);
        rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        user = userRepository.save(user);
        userBis = userRepository.save(createTestUserBis(null));
    }

    @AfterEach
    public void afterEach() {
        userRepository.findAll().forEach(user -> {
            user.setSubscribedProgExercises(new HashSet<>());
            userRepository.save(user);
        });
        progExerciseRepository.deleteAll();
        exerciseRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void getUsers_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.getUsers());
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getUsers_ValidUse_ReturnListOfUserDto() {
        List<UserDto> userDtos = userController.getUsers();

        assertEqualUserList(List.of(user, userBis), userDtos);
    }

    @Test
    void getUserById_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.getUserById(user.getId() + 1));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getUserById_InvalidTargetSetId_ThrowUserNotFoundException() {
        userRepository.delete(user);

        Assertions.assertThrows(UserNotFoundException.class, () -> userController.getUserById(user.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void getUserById_ValidInput_ReturnUserDto() {
        UserDto userDto = userController.getUserById(user.getId());

        assertUserDtoAndEntity(user, userDto, false);
    }

    @Test
    void getUserProgExercises_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.getUserProgExercises(user.getId() + 1));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getUserProgExercises_InvalidUserId_ThrowUserNotFoundException() {
        userRepository.delete(user);

        Assertions.assertThrows(UserNotFoundException.class, () -> userController.getUserProgExercises(user.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getUserProgExercises_ValidUse_ReturnListOfUserDto() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        user.getSubscribedProgExercises().add(progExercise);
        userRepository.save(user);

        List<ProgExerciseDto> progExerciseDtos = userController.getUserProgExercises(user.getId());

        assertProgExerciseDtoAndEntity(progExercise, progExerciseDtos.getFirst());
    }


    @Test
    void modifyUserIdentity_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        InputUserIdentity inputUserIdentity = createTestInputUserIdentity(user.getId());

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.modifyUserIdentity(inputUserIdentity));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyUserIdentity_InvalidUserId_ThrowUserNotFoundException() {
        userRepository.delete(user);

        InputUserIdentity inputUserIdentity = createTestInputUserIdentity(user.getId());

        Assertions.assertThrows(UserNotFoundException.class, () -> userController.modifyUserIdentity(inputUserIdentity));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyUserIdentity_ValidUse_ReturnUserDto() {
        InputUserIdentity inputUserIdentity = createTestInputUserIdentity(user.getId());

        UserDto userDto = userController.modifyUserIdentity(inputUserIdentity);

        Assertions.assertEquals(userDto.getFirstName(), inputUserIdentity.getFirstName());
        Assertions.assertEquals(userDto.getLastName(), inputUserIdentity.getLastName());
    }

    @Test
    void modifyUserRoles_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));
        InputUserRoles inputUserIdentity = createTestInputUserRoles(user.getId());
        inputUserIdentity.getRoleIds().add(role.getId());

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.modifyUserRoles(inputUserIdentity));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void modifyUserRoles_ValidUse_ReturnUserDto() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));
        InputUserRoles inputUserIdentity = createTestInputUserRoles(user.getId());
        inputUserIdentity.getRoleIds().add(role.getId());

        UserDto userDto = userController.modifyUserRoles(inputUserIdentity);

        Assertions.assertEquals(role.getName(), userDto.getRoles().stream().findFirst().orElseThrow().getName());
    }

    @Test
    void modifyUserEmail_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        InputUserEmail inputUserIdentity = createTestInputUserEmail(user.getId(), rawPassword);

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.modifyUserEmail(inputUserIdentity));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyUserEmail_UserEmailAlreadyUsed_ThrowUsernameExistsException() {
        InputUserEmail inputUserIdentity = createTestInputUserEmail(user.getId(), rawPassword);
        inputUserIdentity.setNewEmail(userBis.getEmail());

        Assertions.assertThrows(EmailAlreadyUsedException.class, () -> userController.modifyUserEmail(inputUserIdentity));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyUserEmail_ValidInput_ReturnAuthDto() {
        InputUserEmail inputUserIdentity = createTestInputUserEmail(user.getId(), rawPassword);

        AuthDto authDto = userController.modifyUserEmail(inputUserIdentity);

        assertUserDtoAndEntity(user, authDto.getUser(), true);
        Assertions.assertEquals(inputUserIdentity.getNewEmail(), authDto.getUser().getEmail());
    }

    @Test
    void modifyUserUsername_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        InputUserUsername inputUserUsername = createTestInputUserUsername(user.getId());

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.modifyUserUsername(inputUserUsername));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyUserUsername_UserUsernameAlreadyUsed_ThrowUsernameExistsException() {
        InputUserUsername inputUserUsername = createTestInputUserUsername(user.getId());
        inputUserUsername.setNewUsername(userBis.getUsername());

        Assertions.assertThrows(UsernameExistsException.class, () -> userController.modifyUserUsername(inputUserUsername));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyUserUsername_ValidInput_ReturnUserDto() {
        InputUserUsername inputUserUsername = createTestInputUserUsername(user.getId());

        UserDto userDto = userController.modifyUserUsername(inputUserUsername);

        Assertions.assertEquals(inputUserUsername.getNewUsername(), userDto.getUsername());
    }

    @Test
    void modifyUserPassword_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        InputUserPassword inputUserPassword = createTestInputUserPassword(user.getId(), rawPassword);

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.modifyUserPassword(inputUserPassword));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyUserPassword_ValidUse_ReturnAuthDto() {
        InputUserPassword inputUserPassword = createTestInputUserPassword(user.getId(), rawPassword);

        AuthDto authDto = userController.modifyUserPassword(inputUserPassword);

        assertUserDtoAndEntity(user, authDto.getUser(), false);
        UserEntity modifyUser = userRepository.findById(user.getId()).orElseThrow();
        Assertions.assertTrue(passwordEncoder.matches(inputUserPassword.getNewPassword(), modifyUser.getPassword()));
    }

    @Test
    void deleteUser_NotAuthenticated_ThrowAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.deleteUser(user.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteUser_InvalidUserId_ThrowUnsuccessfulUserNotFound() {
        userRepository.delete(user);

        Assertions.assertThrows(UserNotFoundException.class, () -> userController.deleteUser(user.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void deleteUser_ValidUse_ReturnId() {
        Long id = userController.deleteUser(user.getId());

        Assertions.assertEquals(user.getId(), id);
    }

    private void assertEqualUserList(
            List<UserEntity> userEntities,
            List<UserDto> userDtos
    ) {
        Assertions.assertEquals(userEntities.size(), userDtos.size());
        userDtos.forEach(userDto -> assertUserDtoAndEntity(
                userEntities.stream().filter(
                        userEntity -> Objects.equals(userEntity.getId(), userDto.getId())
                ).toList().getFirst(),
                userDto, false)
        );
    }
}