package com.CptFranck.SportsPeak.integration.repositories;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.repository.ExerciseRepository;
import com.CptFranck.SportsPeak.repository.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUserBis;

@DataJpaTest
public class UserRepositoryIntTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    private UserEntity user;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(createTestUser(null));
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
    }

    @Test
    public void userRepository_FindByEmail_ReturnAllUsers() {
        Optional<UserEntity> foundUser = userRepository.findByEmail(user.getEmail());

        Assertions.assertNotNull(foundUser);
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertNotNull(foundUser.get());
    }

    @Test
    public void userRepository_FindByUsername_ReturnUser() {
        Optional<UserEntity> foundUser = userRepository.findByUsername(user.getUsername());

        Assertions.assertNotNull(foundUser);
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertNotNull(foundUser.get());
    }

    @Test
    public void userRepository_FindAllBySubscribedProgExercisesContaining_ReturnUser() {
        UserEntity subscriber = userRepository.save(createTestUserBis(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        user.getSubscribedProgExercises().add(progExercise);
        user.getProgExercisesCreated().add(progExercise);
        subscriber.getSubscribedProgExercises().add(progExercise);

        List<UserEntity> foundUsers = userRepository.findAllBySubscribedProgExercisesContaining(progExercise);

        Assertions.assertNotNull(foundUsers);
        Assertions.assertTrue(foundUsers.contains(user));
        Assertions.assertTrue(foundUsers.contains(subscriber));
    }
}
