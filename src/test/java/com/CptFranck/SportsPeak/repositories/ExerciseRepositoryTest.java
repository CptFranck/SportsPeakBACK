package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.TestDataUtil;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
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

import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestExercise;
import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestExercises;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ExerciseRepositoryTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    private ExerciseEntity saveOneExerciseInRepository() {
        ExerciseEntity user = createNewTestExercise();
        return exerciseRepository.save(user);
    }

    private List<ExerciseEntity> saveAllExercisesInRepository(List<ExerciseEntity> users) {
        List<ExerciseEntity> localExercises = Objects.requireNonNullElseGet(users, TestDataUtil::createNewTestExercises);
        exerciseRepository.saveAll(localExercises);
        return localExercises;
    }
    
    @Test
    public void ExerciseRepository_Save_ReturnSavedExercise() {
        ExerciseEntity exercise = createNewTestExercise();

        ExerciseEntity savedExercise = exerciseRepository.save(exercise);

        Assertions.assertNotNull(savedExercise);
        Assertions.assertTrue(exercise.getId() > 0L, "exercise Id must be > 0");
    }

    @Test
    public void ExerciseRepository_SaveAll_ReturnSavedExercises() {
        List<ExerciseEntity> users = createNewTestExercises();

        List<ExerciseEntity> savedExercises = saveAllExercisesInRepository(users);

        Assertions.assertNotNull(savedExercises);
        Assertions.assertEquals(users.size(), savedExercises.size());
        Assertions.assertTrue(users.getFirst().getId() > 0L, "first user Id must be > 0");
        Assertions.assertTrue(users.getLast().getId() > 1L, "second user Id must be > 0");
    }

    @Test
    public void ExerciseRepository_FindById_ReturnExercise() {
        ExerciseEntity savedExercise = saveOneExerciseInRepository();

        Optional<ExerciseEntity> foundExercise = exerciseRepository.findById(savedExercise.getId());

        Assertions.assertNotNull(foundExercise);
        Assertions.assertTrue(foundExercise.isPresent());
        Assertions.assertNotNull(foundExercise.get());
    }
    
    @Test
    public void ExerciseRepository_FindAll_ReturnAllExercise() {
        saveAllExercisesInRepository(null);

        List<ExerciseEntity> exerciseTypeEntities = StreamSupport.stream(
                        exerciseRepository.findAll().spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(exerciseTypeEntities);
        Assertions.assertEquals(exerciseTypeEntities.size(), 2);
    }

    @Test
    public void ExerciseRepository_FindAllById_ReturnAllExercises() {
        List<ExerciseEntity> users = createNewTestExercises();
        exerciseRepository.saveAll(users);

        List<ExerciseEntity> userEntities = StreamSupport.stream(
                        exerciseRepository.findAllById(users.stream()
                                        .map(ExerciseEntity::getId)
                                        .toList())
                                .spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(userEntities);
        Assertions.assertEquals(2, userEntities.size());
    }

    @Test
    public void ExerciseRepository_ExistById_ReturnTrue() {
        ExerciseEntity savedExercise = saveOneExerciseInRepository();

        boolean foundExercise = exerciseRepository.existsById(savedExercise.getId());

        Assertions.assertTrue(foundExercise);
    }

    @Test
    public void ExerciseRepository_Count_ReturnExercisesListSize() {
        List<ExerciseEntity> savedExercises = saveAllExercisesInRepository(null);

        Long userCount = exerciseRepository.count();

        Assertions.assertEquals(userCount, savedExercises.size());
    }

    @Test
    public void ExerciseRepository_Delete_ReturnFalse() {
        ExerciseEntity savedExercise = saveOneExerciseInRepository();

        exerciseRepository.delete(savedExercise);
        boolean foundExercise = exerciseRepository.existsById(savedExercise.getId());

        Assertions.assertFalse(foundExercise);
    }

    @Test
    public void ExerciseRepository_DeleteById_ReturnFalse() {
        ExerciseEntity savedExercise = saveOneExerciseInRepository();

        exerciseRepository.deleteById(savedExercise.getId());
        boolean foundExercise = exerciseRepository.existsById(savedExercise.getId());

        Assertions.assertFalse(foundExercise);
    }

    @Test
    public void ExerciseRepository_DeleteAllById_ReturnAllFalse() {
        List<ExerciseEntity> exerciseEntities = saveAllExercisesInRepository(null);

        exerciseRepository.deleteAllById(exerciseEntities.stream().map(ExerciseEntity::getId).toList());

        exerciseEntities.forEach(exercise -> {
            boolean foundExercise = exerciseRepository.existsById(exercise.getId());
            Assertions.assertFalse(foundExercise);
        });
    }

    @Test
    public void ExerciseRepository_DeleteAll_ReturnAllFalse() {
        List<ExerciseEntity> exerciseEntities = saveAllExercisesInRepository(null);

        exerciseRepository.deleteAll();

        exerciseEntities.forEach(exercise -> {
            boolean foundExercise = exerciseRepository.existsById(exercise.getId());
            Assertions.assertFalse(foundExercise);
        });
    }
}
