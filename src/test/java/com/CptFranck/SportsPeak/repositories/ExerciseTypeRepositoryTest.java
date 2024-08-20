package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestExerciseType;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ExerciseTypeRepositoryTest {

    @Autowired
    private ExerciseTypeRepository exerciseTypeRepository;

    @Test
    public void ExerciseTypeRepository_Save_ReturnSavedComplexMuscle() {
        ExerciseTypeEntity exerciseType = createNewTestExerciseType();

        ExerciseTypeEntity savedExerciseType = exerciseTypeRepository.save(exerciseType);

        Assertions.assertNotNull(savedExerciseType);
        Assertions.assertTrue(exerciseType.getId() > 0L, "exerciseType Id must be > 0");
    }

    @Test
    public void ExerciseTypeRepository_findAll_ReturnAllMuscle() {
        ExerciseTypeEntity savedExerciseOne = createNewTestExerciseType();
        ExerciseTypeEntity savedExerciseTwo = createNewTestExerciseType();
        exerciseTypeRepository.save(savedExerciseOne);
        exerciseTypeRepository.save(savedExerciseTwo);

        List<ExerciseTypeEntity> exerciseTypeEntities = StreamSupport.stream(
                        exerciseTypeRepository.findAll().spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(exerciseTypeEntities);
        Assertions.assertEquals(exerciseTypeEntities.size(), 2);
    }

    @Test
    public void ExerciseTypeRepository_findById_ReturnMuscle() {
        ExerciseTypeEntity exerciseType = createNewTestExerciseType();
        ExerciseTypeEntity savedExerciseType = exerciseTypeRepository.save(exerciseType);

        Optional<ExerciseTypeEntity> foundExerciseType = exerciseTypeRepository.findById(savedExerciseType.getId());

        Assertions.assertNotNull(foundExerciseType);
        Assertions.assertTrue(foundExerciseType.isPresent());
        Assertions.assertNotNull(foundExerciseType.get());
    }

    @Test
    public void ExerciseTypeRepository_existById_ReturnTrue() {
        ExerciseTypeEntity exerciseType = createNewTestExerciseType();
        ExerciseTypeEntity savedExerciseType = exerciseTypeRepository.save(exerciseType);

        boolean foundExerciseType = exerciseTypeRepository.existsById(savedExerciseType.getId());

        Assertions.assertTrue(foundExerciseType);
    }

    @Test
    public void ExerciseTypeRepository_deleteById_ReturnTrue() {
        ExerciseTypeEntity exerciseType = createNewTestExerciseType();
        ExerciseTypeEntity savedExerciseType = exerciseTypeRepository.save(exerciseType);

        exerciseTypeRepository.deleteById(savedExerciseType.getId());
        boolean foundExerciseType = exerciseTypeRepository.existsById(savedExerciseType.getId());

        Assertions.assertFalse(foundExerciseType);
    }
}
