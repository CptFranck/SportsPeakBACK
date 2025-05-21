package com.CptFranck.SportsPeak.integration.services.services;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.userAuth.EmailAlreadyUsedException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UserNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UsernameExistsException;
import com.CptFranck.SportsPeak.repository.*;
import com.CptFranck.SportsPeak.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestRoleUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class UserServiceImplIT {

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

    private UserEntity user;
    private UserEntity userBis;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(createTestUser(null));
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
        privilegeRepository.deleteAll();
    }

    @Test
    void findAll_ValidUse_ReturnListOfUserEntity() {
        List<UserEntity> userFound = userServiceImpl.findAll();

        assertEqualsUserList(List.of(user, userBis), userFound);
    }

    @Test
    void findOne_InvalidUserId_ReturnUserEntity() {
        userRepository.delete(user);

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.findOne(user.getId()));
    }

    @Test
    void findOne_ValidUse_ReturnUserEntity() {
        UserEntity userFound = userServiceImpl.findOne(user.getId());

        assertEqualsUser(user, userFound, false);
    }

    @Test
    void findMany_ValidUse_ReturnSetOfUserEntity() {
        Set<UserEntity> usersFound = userServiceImpl.findMany(Set.of(user.getId()));

        assertEqualsUserList(List.of(user), usersFound.stream().toList());
    }

    @Test
    void findUserBySubscribedProgExercises_ValidUse_ReturnSetOfUserEntity() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        user.getSubscribedProgExercises().add(progExercise);
        userBis.getSubscribedProgExercises().add(progExercise);
        user = userRepository.save(user);
        userBis = userRepository.save(userBis);

        Set<UserEntity> userFound = userServiceImpl.findUserBySubscribedProgExercises(progExercise);

        assertEqualsUserList(List.of(user, userBis), userFound.stream().toList());
    }

    @Test
    void findByLogin_InvalidLogin_ThrowUsernameNotFoundException() {
        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.findByLogin("test"));
    }

    @Test
    void findByLogin_ValidEmail_ReturnUserEntity() {
        UserEntity userFound = userServiceImpl.findByLogin(user.getEmail());

        assertEqualsUser(user, userFound, false);
    }

    @Test
    void findByLogin_ValidUsername_ReturnUserEntity() {
        UserEntity userFound = userServiceImpl.findByLogin(user.getUsername());

        assertEqualsUser(user, userFound, false);
    }

    @Test
    void save_AddNewUserWithEmailAlreadyTaken_ThrowEmailAlreadyUsedException() {
        UserEntity userSavedInRepository = createTestUser(null);

        assertThrows(EmailAlreadyUsedException.class, () -> userServiceImpl.save(userSavedInRepository));
    }

    @Test
    void save_AddNewUserWithUsernameAlreadyTaken_ThrowUsernameExistsException() {
        UserEntity userSavedInRepository = createTestUser(null);
        userSavedInRepository.setEmail("email");

        assertThrows(UsernameExistsException.class, () -> userServiceImpl.save(userSavedInRepository));
    }

    @Test
    void save_AddNewUser_ThrowUsernameExistsException() {
        UserEntity userSavedInRepository = createTestUser(null);
        userSavedInRepository.setEmail("email");
        userSavedInRepository.setUsername("username");

        UserEntity userSaved = userServiceImpl.save(userSavedInRepository);

        assertEqualsUser(userSavedInRepository, userSaved, true);
    }

    @Test
    void save_UpdateUserNotExisting_ThrowUserNotFoundException() {
        userRepository.delete(user);

        Assertions.assertThrows(UserNotFoundException.class, () -> userServiceImpl.save(user));
    }

    @Test
    void save_UpdateUserWithEmailAlreadyTaken_ThrowEmailAlreadyUsedException() {
        user.setEmail(userBis.getEmail());

        assertThrows(EmailAlreadyUsedException.class, () -> userServiceImpl.save(user));
    }

    @Test
    void save_UpdateUserWithUsernameAlreadyTaken_ThrowUsernameExistsException() {
        user.setUsername(userBis.getUsername());

        assertThrows(UsernameExistsException.class, () -> userServiceImpl.save(user));
    }

    @Test
    void save_UpdateUser_ReturnUserEntity() {
        UserEntity userSaved = userServiceImpl.save(user);

        assertEqualsUser(user, userSaved, true);
    }

    @Test
    void save_UpdateUserWithNewInformation_ReturnUserEntity() {
        user.setEmail("email");
        user.setUsername("username");
        UserEntity userSaved = userServiceImpl.save(user);

        assertEqualsUser(user, userSaved, true);
    }


    @Test
    void delete_InvalidUserId_ThrowUserNotFoundException() {
        userRepository.delete(user);

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.delete(user.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        assertAll(() -> userServiceImpl.delete(user.getId()));
    }

    @Test
    void updateRoleRelation_ValidInput_Void() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));
        Set<Long> oldUserIds = Set.of(user.getId());
        Set<Long> newUserIds = Set.of(userBis.getId());
        user.getRoles().add(role);
        userRepository.save(user);

        userServiceImpl.updateRoleRelation(newUserIds, oldUserIds, role);

        Optional<UserEntity> userReturn = userRepository.findById(user.getId());
        Optional<UserEntity> userBisReturn = userRepository.findById(userBis.getId());
        assertTrue(userReturn.isPresent());
        assertTrue(userBisReturn.isPresent());
        assertEquals(0, userReturn.get().getRoles().size());
        assertEquals(1, userBisReturn.get().getRoles().size());
        assertEquals(role.getId(), userBisReturn.get().getRoles().stream().toList().getFirst().getId());

    }

    @Test
    void UserService_Exists_Success() {
        boolean userFound = userServiceImpl.exists(user.getId());

        Assertions.assertTrue(userFound);
    }

    private void assertEqualsUserList(
            List<UserEntity> expectedExerciseList,
            List<UserEntity> exerciseListObtained
    ) {
        Assertions.assertEquals(expectedExerciseList.size(), exerciseListObtained.size());
        expectedExerciseList.forEach(userFound -> assertEqualsUser(
                exerciseListObtained.stream().filter(
                        user -> Objects.equals(user.getId(), userFound.getId())
                ).toList().getFirst(),
                userFound, false)
        );
    }
}
