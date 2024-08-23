package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createNewTestProgExerciseList;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ProgExerciseRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Test
    public void progExerciseRepository_FindAllBySubscribedUsersId_ReturnAllProgExercisesWithCreatorId() {
        UserEntity creator = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        List<ProgExerciseEntity> progExercises = createNewTestProgExerciseList(creator, exercise);
        progExerciseRepository.saveAll(progExercises);
        creator.getSubscribedProgExercises().add(progExercises.getFirst());
        userRepository.save(creator);

        List<ProgExerciseEntity> progExerciseEntities = progExerciseRepository.findAllBySubscribedUsersId(creator.getId());

        Assertions.assertNotNull(progExerciseEntities);
        Assertions.assertEquals(1, progExerciseEntities.size());
    }
}
