package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.TestDataUtil;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
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

import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestExerciseType;
import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestExerciseTypes;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ExerciseTypeRepositoryTest {

    @Autowired
    private ExerciseTypeRepository exerciseTypeRepository;

    private ExerciseTypeEntity saveOneExerciseTypeInRepository() {
        ExerciseTypeEntity user = createNewTestExerciseType();
        return exerciseTypeRepository.save(user);
    }

    private List<ExerciseTypeEntity> saveAllExerciseTypesInRepository(List<ExerciseTypeEntity> exerciseType) {
        List<ExerciseTypeEntity> localExerciseTypes = Objects.requireNonNullElseGet(exerciseType, TestDataUtil::createNewTestExerciseTypes);
        exerciseTypeRepository.saveAll(localExerciseTypes);
        return localExerciseTypes;
    }
    
    @Test
    public void ExerciseTypeRepository_Save_ReturnSavedExerciseType() {
        ExerciseTypeEntity exerciseType = createNewTestExerciseType();

        ExerciseTypeEntity savedExerciseType = exerciseTypeRepository.save(exerciseType);

        Assertions.assertNotNull(savedExerciseType);
        Assertions.assertTrue(exerciseType.getId() > 0L, "exerciseType Id must be > 0");
    }

    @Test
    public void ExerciseTypeRepository_SaveAll_ReturnSavedExerciseTypes() {
        List<ExerciseTypeEntity> users = createNewTestExerciseTypes();

        List<ExerciseTypeEntity> savedExerciseTypes = saveAllExerciseTypesInRepository(users);

        Assertions.assertNotNull(savedExerciseTypes);
        Assertions.assertEquals(users.size(), savedExerciseTypes.size());
        Assertions.assertTrue(users.getFirst().getId() > 0L, "first user Id must be > 0");
        Assertions.assertTrue(users.getLast().getId() > 1L, "second user Id must be > 0");
    }

    @Test
    public void ExerciseTypeRepository_FindById_ReturnExerciseType() {
        ExerciseTypeEntity savedExerciseType = saveOneExerciseTypeInRepository();

        Optional<ExerciseTypeEntity> foundExerciseType = exerciseTypeRepository.findById(savedExerciseType.getId());

        Assertions.assertNotNull(foundExerciseType);
        Assertions.assertTrue(foundExerciseType.isPresent());
        Assertions.assertNotNull(foundExerciseType.get());
    }
    
    @Test
    public void ExerciseTypeRepository_FindAll_ReturnAllExerciseType() {
        saveAllExerciseTypesInRepository(null);

        List<ExerciseTypeEntity> exerciseTypeEntities = StreamSupport.stream(
                        exerciseTypeRepository.findAll().spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(exerciseTypeEntities);
        Assertions.assertEquals(exerciseTypeEntities.size(), 2);
    }

    @Test
    public void UserRepository_FindAllById_ReturnAllUsers() {
        List<ExerciseTypeEntity> exerciseTypes = createNewTestExerciseTypes();
        exerciseTypeRepository.saveAll(exerciseTypes);

        List<ExerciseTypeEntity> userEntities = StreamSupport.stream(
                        exerciseTypeRepository.findAllById(exerciseTypes.stream()
                                        .map(ExerciseTypeEntity::getId)
                                        .toList())
                                .spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(userEntities);
        Assertions.assertEquals(2, userEntities.size());
    }

    @Test
    public void ExerciseTypeRepository_ExistById_ReturnTrue() {
        ExerciseTypeEntity savedExerciseType = saveOneExerciseTypeInRepository();

        boolean foundExerciseType = exerciseTypeRepository.existsById(savedExerciseType.getId());

        Assertions.assertTrue(foundExerciseType);
    }

    @Test
    public void ExerciseTypeRepository_Count_ReturnExerciseTypesListSize() {
        List<ExerciseTypeEntity> savedExerciseTypes = saveAllExerciseTypesInRepository(null);

        Long userCount = exerciseTypeRepository.count();

        Assertions.assertEquals(userCount, savedExerciseTypes.size());
    }

    @Test
    public void ExerciseTypeRepository_Delete_ReturnFalse() {
        ExerciseTypeEntity savedExerciseType = saveOneExerciseTypeInRepository();

        exerciseTypeRepository.delete(savedExerciseType);
        boolean foundExerciseType = exerciseTypeRepository.existsById(savedExerciseType.getId());

        Assertions.assertFalse(foundExerciseType);
    }

    @Test
    public void ExerciseTypeRepository_DeleteById_ReturnFalse() {
        ExerciseTypeEntity savedExerciseType = saveOneExerciseTypeInRepository();

        exerciseTypeRepository.deleteById(savedExerciseType.getId());
        boolean foundExerciseType = exerciseTypeRepository.existsById(savedExerciseType.getId());

        Assertions.assertFalse(foundExerciseType);
    }

    @Test
    public void ExerciseTypeRepository_DeleteAllById_ReturnAllFalse() {
        List<ExerciseTypeEntity> exerciseTypeEntities = saveAllExerciseTypesInRepository(null);

        exerciseTypeRepository.deleteAllById(exerciseTypeEntities.stream().map(ExerciseTypeEntity::getId).toList());

        exerciseTypeEntities.forEach(user -> {
            boolean foundExerciseType = exerciseTypeRepository.existsById(user.getId());
            Assertions.assertFalse(foundExerciseType);
        });
    }

    @Test
    public void ExerciseTypeRepository_DeleteAll_ReturnAllFalse() {
        List<ExerciseTypeEntity> exerciseTypeEntities = saveAllExerciseTypesInRepository(null);

        exerciseTypeRepository.deleteAll();

        exerciseTypeEntities.forEach(exerciseType -> {
            boolean foundExerciseType = exerciseTypeRepository.existsById(exerciseType.getId());
            Assertions.assertFalse(foundExerciseType);
        });
    }
}
