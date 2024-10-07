package com.CptFranck.SportsPeak.service.IntegrationTest;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.domain.exception.userAuth.*;
import com.CptFranck.SportsPeak.repositories.*;
import com.CptFranck.SportsPeak.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestPrivilegeUtils.createTestPrivilege;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class UserServiceImplIntTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    public void afterEach() {
        this.progExerciseRepository.deleteAll();
        this.exerciseRepository.deleteAll();
        this.userRepository.deleteAll();
        this.roleRepository.deleteAll();
    }


    @Test
    void UserService_Save_Success() {
        UserEntity userSavedInRepository = createTestUser(null);

        UserEntity userSaved = userServiceImpl.save(userSavedInRepository);

        assertEqualsUser(userSavedInRepository, userSaved);
    }

    @Test
    void UserService_FindAll_Success() {
        List<UserEntity> userList = StreamSupport.stream(
                userRepository.saveAll(createTestUserList(true)).spliterator(),
                false).toList();

        List<UserEntity> userFound = userServiceImpl.findAll();

        assertEqualsUserList(userList, userFound);
    }

    @Test
    void UserService_FindOne_Success() {
        UserEntity user = userRepository.save(createTestUser(null));

        Optional<UserEntity> userFound = userServiceImpl.findOne(user.getId());

        Assertions.assertTrue(userFound.isPresent());
        assertEqualsUser(user, userFound.get());
    }

    @Test
    void UserService_FindMany_Success() {
        List<UserEntity> userList = StreamSupport.stream(
                userRepository.saveAll(createTestUserList(true)).spliterator(),
                false).toList();
        Set<Long> userIds = userList.stream().map(UserEntity::getId).collect(Collectors.toSet());

        Set<UserEntity> usersFound = userServiceImpl.findMany(userIds);

        assertEqualsUserList(userList, usersFound.stream().toList());
    }

    @Test
    @Transactional
    void UserService_FindUserBySubscribedProgExercises_Success() {
        UserEntity user = userRepository.save(createTestUser(null));
        UserEntity userBis = userRepository.save(createTestUserBis(null));
        List<UserEntity> userList = List.of(user, userBis);
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        user.getSubscribedProgExercises().add(progExercise);
        userBis.getSubscribedProgExercises().add(progExercise);
        userRepository.save(user);
        userRepository.save(userBis);

        Set<UserEntity> userFound = userServiceImpl.findUserBySubscribedProgExercises(progExercise);

        assertEqualsUserList(userList, userFound.stream().toList());
    }

    @Test
    @Transactional
    void exerciseService_UpdateRoleRelation_Success() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));
        UserEntity user = userRepository.save(createTestUser(null));
        UserEntity userBis = userRepository.save(createTestUserBis(null));
        Set<Long> oldUserIds = new HashSet<>();
        Set<Long> newUserIds = new HashSet<>();
        oldUserIds.add(user.getId());
        newUserIds.add(userBis.getId());
        user.getRoles().add(role);
        userRepository.save(user);

        userServiceImpl.updateRoleRelation(newUserIds, oldUserIds, role);

        assertEquals(0, user.getRoles().size());
        assertEquals(1, userBis.getRoles().size());
        assertEquals(role.getId(), userBis.getRoles().stream().toList().getFirst().getId());

    }

    @Test
    void UserService_Exists_Success() {
        UserEntity user = userRepository.save(createTestUser(null));

        boolean userFound = userServiceImpl.exists(user.getId());

        Assertions.assertTrue(userFound);
    }

    @Test
    void UserService_Delete_Success() {
        UserEntity user = userRepository.save(createTestUser(null));

        assertAll(() -> userServiceImpl.delete(user.getId()));
    }

    @Test
    void UserService_Delete_Unsuccessful() {
        UserEntity user = userRepository.save(createTestUser(null));
        userRepository.delete(user);

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.delete(user.getId()));
    }

    @Test
    @Transactional
    void UserService_ChangeIdentity_Unsuccessful() {

        assertThrows(EmailUnknownException.class,
                () -> userServiceImpl.changeIdentity(1L, "firstName", "lastName"));
    }

    @Test
    void UserService_ChangeIdentity_Successful() {
        UserEntity user = userRepository.save(createTestUser(null));

        UserEntity userSaved = userServiceImpl.changeIdentity(user.getId(), "firstName", "lastName");

        Assertions.assertEquals(user.getId(), userSaved.getId());
        Assertions.assertEquals(user.getEmail(), userSaved.getEmail());
        Assertions.assertNotEquals(user.getFirstName(), userSaved.getFirstName());
        Assertions.assertNotEquals(user.getLastName(), userSaved.getLastName());
        Assertions.assertEquals(user.getUsername(), userSaved.getUsername());
        Assertions.assertEquals(user.getPassword(), userSaved.getPassword());
    }

    @Test
    void UserService_ChangeRoles_Unsuccessful() {
        assertThrows(EmailUnknownException.class,
                () -> userServiceImpl.changeRoles(1L, new HashSet<>()));
    }

    @Test
    void UserService_ChangeRoles_Successful() {
        UserEntity user = userRepository.save(createTestUser(null));
        RoleEntity role = roleRepository.save(createTestRole(null, 1));
        RoleEntity roleBis = roleRepository.save(createTestRole(null, 2));
        Set<RoleEntity> userRoles = new HashSet<RoleEntity>();
        Set<RoleEntity> newRoles = new HashSet<RoleEntity>();
        userRoles.add(role);
        newRoles.add(roleBis);
        user.getRoles().addAll(userRoles);
        userRepository.save(user);

        UserEntity savedUser = userServiceImpl.changeRoles(user.getId(), newRoles);

        assertEqualsUser(user, savedUser);
        Assertions.assertNotEquals(
                user.getRoles().stream().map(RoleEntity::getId).collect(Collectors.toSet()),
                savedUser.getRoles().stream().map(RoleEntity::getId).collect(Collectors.toSet())
        );
    }

    @Test
    void UserService_ChangeEmail_UnsuccessfulUserNotFound() {
        assertThrows(UserNotFoundException.class,
                () -> userServiceImpl.changeEmail(1L, "password", "newEmail"));
    }

    @Test
    void UserService_ChangeEmail_UnsuccessfulEmailAlreadyUsed() {
        UserEntity user = createTestUser(null);
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        userServiceImpl.save(user);

        assertThrows(EmailAlreadyUsedException.class,
                () -> userServiceImpl.changeEmail(user.getId(), "password", user.getEmail()));
    }

    @Test
    void UserService_ChangeEmail_UnsuccessfulIncorrectPassword() {
        UserEntity user = createTestUser(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userServiceImpl.save(user);

        assertThrows(IncorrectPasswordException.class,
                () -> userServiceImpl.changeEmail(user.getId(), "wrongPassword", "newEmail"));
    }

    @Test
    void UserService_ChangeEmail_Successful() {
        UserEntity user = createTestUser(null);
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        userServiceImpl.save(user);

        UserEntity userSaved = userServiceImpl.changeEmail(user.getId(), rawPassword, "newEmail");

        Assertions.assertEquals(user.getId(), userSaved.getId());
        Assertions.assertNotEquals(user.getEmail(), userSaved.getEmail());
        Assertions.assertEquals(user.getFirstName(), userSaved.getFirstName());
        Assertions.assertEquals(user.getLastName(), userSaved.getLastName());
        Assertions.assertEquals(user.getUsername(), userSaved.getUsername());
        Assertions.assertEquals(user.getPassword(), userSaved.getPassword());
    }

    @Test
    void UserService_ChangeUsername_UnsuccessfulUserNotFound() {
        assertThrows(UserNotFoundException.class,
                () -> userServiceImpl.changeUsername(1L, "newUsername"));
    }

    @Test
    void UserService_ChangeUsername_UnsuccessfulUsernameAlreadyUsed() {
        UserEntity user = userRepository.save(createTestUser(null));
        UserEntity userBis = userRepository.save(createTestUserBis(null));

        assertThrows(UsernameExistsException.class,
                () -> userServiceImpl.changeUsername(user.getId(), userBis.getUsername()));
    }

    @Test
    void UserService_ChangeUsername_Successful() {
        UserEntity user = userRepository.save(createTestUser(null));

        UserEntity userSaved = userServiceImpl.changeUsername(user.getId(), "newUsername");

        Assertions.assertEquals(user.getId(), userSaved.getId());
        Assertions.assertEquals(user.getEmail(), userSaved.getEmail());
        Assertions.assertEquals(user.getFirstName(), userSaved.getFirstName());
        Assertions.assertEquals(user.getLastName(), userSaved.getLastName());
        Assertions.assertNotEquals(user.getUsername(), userSaved.getUsername());
        Assertions.assertEquals(user.getPassword(), userSaved.getPassword());
    }

    @Test
    void UserService_ChangePassword_UnsuccessfulUserNotFound() {
        assertThrows(UserNotFoundException.class,
                () -> userServiceImpl.changePassword(1L, "oldPassword", "newPassword"));
    }

    @Test
    void UserService_ChangePassword_UnsuccessfulIncorrectPassword() {
        UserEntity user = createTestUser(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userServiceImpl.save(user);

        assertThrows(IncorrectPasswordException.class,
                () -> userServiceImpl.changePassword(user.getId(), "wrongPassword", "newPassword"));
    }

    @Test
    void UserService_ChangePassword_Successful() {
        UserEntity user = createTestUser(null);
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        userServiceImpl.save(user);

        UserEntity userSaved = userServiceImpl.changePassword(user.getId(), rawPassword, "newPassword");

        Assertions.assertEquals(user.getId(), userSaved.getId());
        Assertions.assertEquals(user.getEmail(), userSaved.getEmail());
        Assertions.assertEquals(user.getFirstName(), userSaved.getFirstName());
        Assertions.assertEquals(user.getLastName(), userSaved.getLastName());
        Assertions.assertEquals(user.getUsername(), userSaved.getUsername());
        Assertions.assertNotEquals(user.getPassword(), userSaved.getPassword());
    }

    @Test
    void UserService_LoadUserByUsername_UnsuccessfulUsernameNotFound() {

        assertThrows(UsernameNotFoundException.class,
                () -> userServiceImpl.loadUserByUsername("email"));
    }

    @Test
    void UserService_LoadUserByUsername_Successful() {
        UserEntity user = userRepository.save(createTestUser(null));
        RoleEntity role = roleRepository.save(createTestRole(null, 0));
        user.getRoles().add(role);
        userRepository.save(user);
        PrivilegeEntity privilege = privilegeRepository.save(createTestPrivilege(null, 0));
        role.getPrivileges().add(privilege);
        privilegeRepository.save(privilege);

        UserDetails userSaved = userServiceImpl.loadUserByUsername(user.getEmail());

        Assertions.assertEquals(user.getEmail(), userSaved.getUsername());
    }

    private void assertEqualsUserList(
            List<UserEntity> expectedExerciseList,
            List<UserEntity> exerciseListObtained
    ) {
        expectedExerciseList.forEach(userFound -> assertEqualsUser(
                exerciseListObtained.stream().filter(
                        user -> Objects.equals(user.getId(), userFound.getId())
                ).toList().getFirst(),
                userFound)
        );
    }
}
