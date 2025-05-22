package com.CptFranck.SportsPeak.unit.services.services;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.userAuth.EmailAlreadyUsedException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UserNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UsernameExistsException;
import com.CptFranck.SportsPeak.repository.UserRepository;
import com.CptFranck.SportsPeak.service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.RoleTestUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUserList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepository;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = createTestUser(1L);
    }

    @Test
    void findAll_ValidUse_ReturnListOfUserEntity() {
        List<UserEntity> userList = createTestUserList(false);
        when(userRepository.findAll()).thenReturn(userList);

        List<UserEntity> userFound = userServiceImpl.findAll();

        Assertions.assertEquals(userList, userFound);
    }

    @Test
    void findOne_InvalidUserId_ReturnUserEntity() {
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.findOne(user.getId()));

    }

    @Test
    void findOne_ValidUse_ReturnUserEntity() {
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));

        UserEntity userFound = userServiceImpl.findOne(user.getId());

        Assertions.assertEquals(user, userFound);
    }

    @Test
    void findMany_ValidUse_ReturnSetOfUserEntity() {
        List<UserEntity> userList = createTestUserList(false);
        Set<Long> userIds = userList.stream().map(UserEntity::getId).collect(Collectors.toSet());
        when(userRepository.findAllById(Mockito.anyIterable())).thenReturn(userList);

        Set<UserEntity> userFound = userServiceImpl.findMany(userIds);

        Assertions.assertEquals(new HashSet<>(userList), userFound);
    }

    @Test
    void findUserBySubscribedProgExercises_ValidUse_ReturnSetOfUserEntity() {
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        List<UserEntity> userList = createTestUserList(false);
        userList.forEach(user -> user.getSubscribedProgExercises().add(progExercise));
        when(userRepository.findAllBySubscribedProgExercisesContaining(Mockito.any(ProgExerciseEntity.class))).thenReturn(userList);

        Set<UserEntity> userFound = userServiceImpl.findUserBySubscribedProgExercises(progExercise);

        Assertions.assertEquals(new HashSet<>(userList), userFound);
    }

    @Test
    void findByLogin_InvalidLogin_ThrowUsernameNotFoundException() {
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.findByLogin("test"));
    }

    @Test
    void findByLogin_ValidEmail_ReturnUserEntity() {
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));

        UserEntity userFound = userServiceImpl.findByLogin(user.getEmail());

        Assertions.assertEquals(user, userFound);
    }

    @Test
    void findByLogin_ValidUsername_ReturnUserEntity() {
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.of(user));

        UserEntity userFound = userServiceImpl.findByLogin(user.getUsername());

        Assertions.assertEquals(user, userFound);
    }

    @Test
    void save_AddNewUserWithEmailAlreadyTaken_ThrowEmailAlreadyUsedException() {
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.empty());

        UserEntity userSavedInRepository = createTestUser(null);

        assertThrows(EmailAlreadyUsedException.class, () -> userServiceImpl.save(userSavedInRepository));
    }

    @Test
    void save_AddNewUserWithUsernameAlreadyTaken_ThrowUsernameExistsException() {
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.of(user));

        UserEntity userSavedInRepository = createTestUser(null);

        assertThrows(UsernameExistsException.class, () -> userServiceImpl.save(userSavedInRepository));
    }

    @Test
    void save_AddNewUser_ThrowUsernameExistsException() {
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);

        UserEntity userSavedInRepository = createTestUser(null);
        UserEntity userSaved = userServiceImpl.save(userSavedInRepository);

        Assertions.assertEquals(user, userSaved);
    }

    @Test
    void save_UpdateUserNotExisting_ThrowUserNotFoundException() {
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.existsById(Mockito.any(Long.class))).thenReturn(false);
        UserEntity userBis = createTestUser(2L);

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.save(userBis));
    }

    @Test
    void save_UpdateUserWithEmailAlreadyTaken_ThrowEmailAlreadyUsedException() {
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        UserEntity userBis = createTestUser(2L);

        assertThrows(EmailAlreadyUsedException.class, () -> userServiceImpl.save(userBis));
    }

    @Test
    void save_UpdateUserWithUsernameAlreadyTaken_ThrowUsernameExistsException() {
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.of(user));
        when(userRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        UserEntity userBis = createTestUser(2L);

        assertThrows(UsernameExistsException.class, () -> userServiceImpl.save(userBis));
    }

    @Test
    void save_UpdateUserWithSameUsernameEmail_ReturnUserEntity() {
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.of(user));
        when(userRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);

        UserEntity userSaved = userServiceImpl.save(user);

        Assertions.assertEquals(user, userSaved);
    }

    @Test
    void save_UpdateUser_ReturnUserEntity() {
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(userRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);

        UserEntity userSaved = userServiceImpl.save(user);

        Assertions.assertEquals(user, userSaved);
    }

    @Test
    void delete_InvalidUserId_ThrowUserNotFoundException() {
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.delete(user.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));

        assertAll(() -> userServiceImpl.delete(user.getId()));
    }

    @Test
    void updateRoleRelation_ValidInput_Void() {
        UserEntity userOne = createTestUser(1L);
        UserEntity userTwo = createTestUser(2L);
        Set<Long> oldUserIds = Set.of(userOne.getId());
        Set<Long> newUserIds = Set.of(userTwo.getId());

        RoleEntity role = createTestRole(1L, 0);
        userOne.getRoles().add(role);

        when(userRepository.findAllById(oldUserIds)).thenReturn(List.of(userOne));
        when(userRepository.findAllById(newUserIds)).thenReturn(List.of(userTwo));
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(userOne)).thenReturn(Optional.of(userTwo));
        when(userRepository.findByUsername(Mockito.any(String.class))).thenReturn(Optional.of(userOne)).thenReturn(Optional.of(userTwo));
        when(userRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        assertAll(() -> userServiceImpl.updateRoleRelation(newUserIds, oldUserIds, role));
    }

    @Test
    void exists_ValidInput_ReturnTrue() {
        when(userRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        boolean userFound = userServiceImpl.exists(user.getId());

        Assertions.assertTrue(userFound);
    }
}
