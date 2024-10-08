package com.CptFranck.SportsPeak.repositories.IntegrationTest;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.repositories.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUserBis;

@DataJpaTest
public class UserRepositoryIntTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    private UserEntity saveOneUserInRepository() {
        UserEntity user = createTestUser(null);
        return userRepository.save(user);
    }

    @Test
    public void userRepository_FindByEmail_ReturnAllUsers() {
        UserEntity savedUser = saveOneUserInRepository();

        Optional<UserEntity> foundUser = userRepository.findByEmail(savedUser.getEmail());

        Assertions.assertNotNull(foundUser);
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertNotNull(foundUser.get());
    }

    @Test
    public void userRepository_FindByUsername_ReturnUser() {
        UserEntity savedUser = saveOneUserInRepository();

        Optional<UserEntity> foundUser = userRepository.findByUsername(savedUser.getUsername());

        Assertions.assertNotNull(foundUser);
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertNotNull(foundUser.get());
    }

    @Test
    public void userRepository_FindAllBySubscribedProgExercisesContaining_ReturnUser() {
        UserEntity creator = saveOneUserInRepository();
        UserEntity subscriber = userRepository.save(createTestUserBis(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(1L));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(1L, creator, exercise));
        creator.getSubscribedProgExercises().add(progExercise);
        creator.getProgExercisesCreated().add(progExercise);
        subscriber.getSubscribedProgExercises().add(progExercise);

        List<UserEntity> foundUsers = userRepository.findAllBySubscribedProgExercisesContaining(progExercise);

        Assertions.assertNotNull(foundUsers);
        Assertions.assertTrue(foundUsers.contains(creator));
        Assertions.assertTrue(foundUsers.contains(subscriber));
    }
}
