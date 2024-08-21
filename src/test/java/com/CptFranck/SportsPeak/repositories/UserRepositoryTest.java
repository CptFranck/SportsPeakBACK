package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.TestDataUtil;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestUser;
import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestUsers;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserEntity saveOneUserInRepository() {
        UserEntity user = createNewTestUser(0);
        return userRepository.save(user);
    }

    private List<UserEntity> saveAllUsersInRepository(List<UserEntity> users) {
        List<UserEntity> localUsers = Objects.requireNonNullElseGet(users, TestDataUtil::createNewTestUsers);
        userRepository.saveAll(localUsers);
        return localUsers;
    }

    @Test
    public void UserRepository_Save_ReturnSavedUser() {
        UserEntity user = createNewTestUser(0);

        UserEntity savedUser = userRepository.save(user);

        Assertions.assertNotNull(savedUser);
        Assertions.assertTrue(user.getId() > 0L, "user Id must be > 0");
    }

    @Test
    public void UserRepository_SaveAll_ReturnSavedUsers() {
        List<UserEntity> users = createNewTestUsers();

        List<UserEntity> savedUsers = saveAllUsersInRepository(users);

        Assertions.assertNotNull(savedUsers);
        Assertions.assertEquals(users.size(), savedUsers.size());
        Assertions.assertTrue(users.getFirst().getId() > 0L, "first user Id must be > 0");
        Assertions.assertTrue(users.getLast().getId() > 1L, "second user Id must be > 0");
    }

    @Test
    public void UserRepository_FindById_ReturnUser() {
        UserEntity savedUser = saveOneUserInRepository();

        Optional<UserEntity> foundUser = userRepository.findById(savedUser.getId());

        Assertions.assertNotNull(foundUser);
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertNotNull(foundUser.get());
    }

    @Test
    public void UserRepository_FindByEmail_ReturnAllUsers() {
        UserEntity savedUser = saveOneUserInRepository();

        Optional<UserEntity> foundUser = userRepository.findByEmail(savedUser.getEmail());

        Assertions.assertNotNull(foundUser);
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertNotNull(foundUser.get());
    }

    @Test
    public void UserRepository_FindByUsername_ReturnUser() {
        UserEntity savedUser = saveOneUserInRepository();

        Optional<UserEntity> foundUser = userRepository.findByUsername(savedUser.getUsername());

        Assertions.assertNotNull(foundUser);
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertNotNull(foundUser.get());
    }

    @Test
    public void UserRepository_FindAll_ReturnUser() {
        saveAllUsersInRepository(null);

        List<UserEntity> userEntities = StreamSupport.stream(
                        userRepository.findAll().spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(userEntities);
        Assertions.assertEquals(userEntities.size(), 2);
    }

    @Test
    public void UserRepository_FindAllById_ReturnAllUsers() {
        List<UserEntity> users = createNewTestUsers();
        userRepository.saveAll(users);

        List<UserEntity> userEntities = StreamSupport.stream(
                        userRepository.findAllById(users.stream()
                                        .map(UserEntity::getId)
                                        .toList())
                                .spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(userEntities);
        Assertions.assertEquals(2, userEntities.size());
    }

    @Test
    public void UserRepository_ExistById_ReturnTrue() {
        UserEntity savedUser = saveOneUserInRepository();

        boolean foundUser = userRepository.existsById(savedUser.getId());

        Assertions.assertTrue(foundUser);
    }

    @Test
    public void UserRepository_Count_ReturnUsersListSize() {
        List<UserEntity> savedUsers = saveAllUsersInRepository(null);

        Long userCount = userRepository.count();

        Assertions.assertEquals(userCount, savedUsers.size());
    }

    @Test
    public void UserRepository_Delete_ReturnFalse() {
        UserEntity savedUser = saveOneUserInRepository();

        userRepository.delete(savedUser);
        boolean foundUser = userRepository.existsById(savedUser.getId());

        Assertions.assertFalse(foundUser);
    }

    @Test
    public void UserRepository_DeleteById_ReturnFalse() {
        UserEntity savedUser = saveOneUserInRepository();

        userRepository.deleteById(savedUser.getId());
        boolean foundUser = userRepository.existsById(savedUser.getId());

        Assertions.assertFalse(foundUser);
    }

    @Test
    public void UserRepository_DeleteAllById_ReturnAllFalse() {
        List<UserEntity> userEntities = saveAllUsersInRepository(null);

        userRepository.deleteAllById(userEntities.stream().map(UserEntity::getId).toList());

        userEntities.forEach(user -> {
            boolean foundUser = userRepository.existsById(user.getId());
            Assertions.assertFalse(foundUser);
        });
    }

    @Test
    public void UserRepository_DeleteAll_ReturnAllFalse() {
        List<UserEntity> userEntities = saveAllUsersInRepository(null);

        userRepository.deleteAll();

        userEntities.forEach(user -> {
            boolean foundUser = userRepository.existsById(user.getId());
            Assertions.assertFalse(foundUser);
        });
    }
}
