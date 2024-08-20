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
    public void MuscleRepository_Save_ReturnSavedMuscle() {
        ExerciseTypeEntity exerciseType = createNewTestExerciseType();

        ExerciseTypeEntity savedExerciseType = exerciseTypeRepository.save(exerciseType);

        Assertions.assertNotNull(savedExerciseType);
        Assertions.assertTrue(exerciseType.getId() > 0L, "Id > 0");
    }

    @Test
    public void MuscleRepository_findAll_ReturnAllMuscle() {
        ExerciseTypeEntity savedExerciseOne = createNewTestExerciseType();
        ExerciseTypeEntity savedExerciseTwo = createNewTestExerciseType();
        exerciseTypeRepository.save(savedExerciseOne);
        exerciseTypeRepository.save(savedExerciseTwo);

        List<ExerciseTypeEntity> muscleEntities = StreamSupport.stream(
                        exerciseTypeRepository.findAll().spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(muscleEntities);
        Assertions.assertEquals(muscleEntities.size(), 2);
    }

    @Test
    public void MuscleRepository_findById_ReturnMuscle() {
        ExerciseTypeEntity exerciseType = createNewTestExerciseType();
        ExerciseTypeEntity savedExerciseType = exerciseTypeRepository.save(exerciseType);

        Optional<ExerciseTypeEntity> foundMuscle = exerciseTypeRepository.findById(savedExerciseType.getId());

        Assertions.assertNotNull(foundMuscle);
        Assertions.assertTrue(foundMuscle.isPresent());
        Assertions.assertNotNull(foundMuscle.get());
    }

    @Test
    public void MuscleRepository_existById_ReturnTrue() {
        ExerciseTypeEntity exerciseType = createNewTestExerciseType();
        ExerciseTypeEntity savedExerciseType = exerciseTypeRepository.save(exerciseType);

        boolean foundMuscle = exerciseTypeRepository.existsById(savedExerciseType.getId());

        Assertions.assertTrue(foundMuscle);
    }

    @Test
    public void MuscleRepository_deleteById_ReturnTrue() {
        ExerciseTypeEntity exerciseType = createNewTestExerciseType();
        ExerciseTypeEntity savedExerciseType = exerciseTypeRepository.save(exerciseType);

        exerciseTypeRepository.deleteById(savedExerciseType.getId());
        boolean foundMuscle = exerciseTypeRepository.existsById(savedExerciseType.getId());

        Assertions.assertFalse(foundMuscle);
    }
}
