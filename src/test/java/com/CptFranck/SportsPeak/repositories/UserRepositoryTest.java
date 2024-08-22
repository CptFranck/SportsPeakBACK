package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestUser;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserEntity saveOneUserInRepository() {
        UserEntity user = createNewTestUser(0);
        return userRepository.save(user);
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
}
