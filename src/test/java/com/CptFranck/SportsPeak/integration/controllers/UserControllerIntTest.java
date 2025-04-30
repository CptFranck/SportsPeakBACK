package com.CptFranck.SportsPeak.integration.controllers;

import com.CptFranck.SportsPeak.controller.UserController;
import com.CptFranck.SportsPeak.domain.dto.AuthDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UserNotFoundException;
import com.CptFranck.SportsPeak.domain.input.user.*;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.repositories.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repositories.RoleRepository;
import com.CptFranck.SportsPeak.repositories.UserRepository;
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

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class UserControllerIntTest {

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
    private String rawPassword;

    @BeforeEach
    void setUp() {
        user = createTestUser(null);
        rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
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
    void UserController_GetUsers_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.getUsers());
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void UserController_GetUsers_Success() {
        List<UserDto> userDtos = userController.getUsers();

        assertEqualUserList(List.of(user), userDtos);
    }

    @Test
    void UserController_GetUserById_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.getUserById(user.getId() + 1));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void UserController_GetUserById_UnsuccessfulUserNotFound() {
        Assertions.assertThrows(UserNotFoundException.class, () -> userController.getUserById(user.getId() + 1));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void UserController_GetUserById_Success() {
        UserDto userDto = userController.getUserById(user.getId());

        assertUserDtoAndEntity(user, userDto);
    }

    @Test
    void UserController_GetUserProgExercises_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.getUserProgExercises(user.getId() + 1));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void UserController_GetUserProgExercises_UnsuccessfulUserNotFound() {
        Assertions.assertThrows(UserNotFoundException.class, () -> userController.getUserProgExercises(user.getId() + 1));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void UserController_GetUserProgExercises_Success() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        user.getSubscribedProgExercises().add(progExercise);
        userRepository.save(user);

        List<ProgExerciseDto> progExerciseDtos = userController.getUserProgExercises(user.getId());

        Assertions.assertEquals(progExercise.getId(), progExerciseDtos.getFirst().getId());
        Assertions.assertEquals(progExercise.getName(), progExerciseDtos.getFirst().getName());
        Assertions.assertEquals(progExercise.getNote(), progExerciseDtos.getFirst().getNote());
        Assertions.assertEquals(progExercise.getVisibility().label, progExerciseDtos.getFirst().getVisibility());
        Assertions.assertEquals(progExercise.getTrustLabel().label, progExerciseDtos.getFirst().getTrustLabel());
        Assertions.assertEquals(progExercise.getCreator().getId(), progExerciseDtos.getFirst().getCreator().getId());
        Assertions.assertEquals(progExercise.getExercise().getId(), progExerciseDtos.getFirst().getExercise().getId());
        Assertions.assertEquals(progExercise.getTargetSets().size(), progExerciseDtos.getFirst().getTargetSets().size());
    }

    @Test
    void UserController_ModifyUserIdentity_UnsuccessfulNotAuthenticated() {
        InputUserIdentity inputUserIdentity = createTestInputUserIdentity(user.getId());

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.modifyUserIdentity(inputUserIdentity));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void UserController_ModifyUserIdentity_Success() {
        InputUserIdentity inputUserIdentity = createTestInputUserIdentity(user.getId());

        UserDto userDto = userController.modifyUserIdentity(inputUserIdentity);

        Assertions.assertEquals(userDto.getFirstName(), inputUserIdentity.getFirstName());
        Assertions.assertEquals(userDto.getLastName(), inputUserIdentity.getLastName());
    }

    @Test
    void UserController_ModifyUserRoles_UnsuccessfulNotAuthenticated() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));
        InputUserRoles inputUserIdentity = createTestInputUserRoles(user.getId());
        inputUserIdentity.getRoleIds().add(role.getId());

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.modifyUserRoles(inputUserIdentity));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void UserController_ModifyUserRoles_Success() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));
        InputUserRoles inputUserIdentity = createTestInputUserRoles(user.getId());
        inputUserIdentity.getRoleIds().add(role.getId());

        UserDto userDto = userController.modifyUserRoles(inputUserIdentity);

        Assertions.assertEquals(role.getName(), userDto.getRoles().stream().findFirst().orElseThrow().getName());
    }

    @Test
    void UserController_ModifyUserEmail_UnsuccessfulNotAuthenticated() {
        InputUserEmail inputUserIdentity = createTestInputUserEmail(user.getId(), rawPassword);

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.modifyUserEmail(inputUserIdentity));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void UserController_ModifyUserEmail_Success() {
        InputUserEmail inputUserIdentity = createTestInputUserEmail(user.getId(), rawPassword);

        AuthDto authDto = userController.modifyUserEmail(inputUserIdentity);

        Assertions.assertEquals(inputUserIdentity.getNewEmail(), authDto.getUser().getEmail());
    }

    @Test
    void UserController_ModifyUserUsername_UnsuccessfulNotAuthenticated() {
        InputUserUsername testInputUserUsername = createTestInputUserUsername(user.getId());

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.modifyUserUsername(testInputUserUsername));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void UserController_ModifyUserUsername_Success() {
        InputUserUsername testInputUserUsername = createTestInputUserUsername(user.getId());

        UserDto userDto = userController.modifyUserUsername(testInputUserUsername);

        Assertions.assertEquals(testInputUserUsername.getNewUsername(), userDto.getUsername());
    }

    @Test
    void UserController_ModifyUserPassword_UnsuccessfulNotAuthenticated() {
        InputUserPassword inputUserPassword = createTestInputUserPassword(user.getId(), rawPassword);

        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.modifyUserPassword(inputUserPassword));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void UserController_ModifyUserPassword_Success() {
        InputUserPassword inputUserPassword = createTestInputUserPassword(user.getId(), rawPassword);

        userController.modifyUserPassword(inputUserPassword);

        UserEntity modifyUser = userRepository.findById(user.getId()).orElseThrow();
        Assertions.assertTrue(passwordEncoder.matches(inputUserPassword.getNewPassword(), modifyUser.getPassword()));
    }

    @Test
    void UserController_DeleteUser_UnsuccessfulNotAuthenticated() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> userController.deleteUser(user.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void UserController_DeleteUser_UnsuccessfulUserNotFound() {
        Assertions.assertThrows(UserNotFoundException.class, () -> userController.deleteUser(user.getId() + 1));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void UserController_DeleteUser_Success() {
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
                userDto)
        );
    }

    private void assertUserDtoAndEntity(UserEntity userEntity, UserDto userDto) {
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(userEntity.getEmail(), userDto.getEmail());
        Assertions.assertEquals(userEntity.getFirstName(), userDto.getFirstName());
        Assertions.assertEquals(userEntity.getLastName(), userDto.getLastName());
        Assertions.assertEquals(userEntity.getUsername(), userDto.getUsername());
        Assertions.assertEquals(userEntity.getRoles().size(), userDto.getRoles().size());
    }
}