package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestExercise;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ExerciseRepositoryTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Test
    public void ExerciseRepository_Save_ReturnSavedExercise() {
        ExerciseEntity exercise = createNewTestExercise();

        ExerciseEntity savedExercise = exerciseRepository.save(exercise);

        Assertions.assertNotNull(savedExercise);
        Assertions.assertTrue(exercise.getId() > 0L, "exercise Id must be > 0");
    }

    @Test
    public void ExerciseRepository_FindAll_ReturnAllExercise() {
        ExerciseEntity exerciseOne = createNewTestExercise();
        ExerciseEntity exerciseTwo = createNewTestExercise();
        exerciseRepository.save(exerciseOne);
        exerciseRepository.save(exerciseTwo);

        List<ExerciseEntity> exerciseTypeEntities = StreamSupport.stream(
                        exerciseRepository.findAll().spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(exerciseTypeEntities);
        Assertions.assertEquals(exerciseTypeEntities.size(), 2);
    }

    @Test
    public void ExerciseRepository_FindById_ReturnExercise() {
        ExerciseEntity exercise = createNewTestExercise();
        ExerciseEntity savedExercise = exerciseRepository.save(exercise);

        Optional<ExerciseEntity> foundExercise = exerciseRepository.findById(savedExercise.getId());

        Assertions.assertNotNull(foundExercise);
        Assertions.assertTrue(foundExercise.isPresent());
        Assertions.assertNotNull(foundExercise.get());
    }

    @Test
    public void ExerciseRepository_ExistById_ReturnTrue() {
        ExerciseEntity exercise = createNewTestExercise();
        ExerciseEntity savedExercise = exerciseRepository.save(exercise);

        boolean foundExercise = exerciseRepository.existsById(savedExercise.getId());

        Assertions.assertTrue(foundExercise);
    }

    @Test
    public void ExerciseRepository_DeleteById_ReturnTrue() {
        ExerciseEntity exercise = createNewTestExercise();
        ExerciseEntity savedExercise = exerciseRepository.save(exercise);

        exerciseRepository.deleteById(savedExercise.getId());
        boolean foundExercise = exerciseRepository.existsById(savedExercise.getId());

        Assertions.assertFalse(foundExercise);
    }
}
