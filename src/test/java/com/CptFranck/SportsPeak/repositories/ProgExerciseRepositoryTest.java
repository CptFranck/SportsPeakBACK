package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ProgExerciseRepositoryTest {

    private UserEntity creator;

    private ExerciseEntity exercise;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    private ProgExerciseEntity saveOneProgExerciseInRepository() {
        ProgExerciseEntity progExercise = createTestNewProgExercise(creator, exercise);
        return progExerciseRepository.save(progExercise);
    }

    private List<ProgExerciseEntity> saveAllProgExercisesInRepository(List<ProgExerciseEntity> progExercises) {
        List<ProgExerciseEntity> localProgExercises = Objects.requireNonNullElseGet(
                progExercises,
                () -> createNewTestProgExercises(creator, exercise));
        progExerciseRepository.saveAll(localProgExercises);
        return localProgExercises;
    }

    @BeforeEach
    public void init() {
        creator = userRepository.save(createNewTestUser(0));
        exercise = exerciseRepository.save(createNewTestExercise());
    }

    @Test
    public void ProgExerciseRepository_Save_ReturnSavedProgExercise() {
        ProgExerciseEntity progExercise = createTestNewProgExercise(creator, exercise);

        ProgExerciseEntity savedProgExercise = progExerciseRepository.save(progExercise);

        Assertions.assertNotNull(savedProgExercise);
        Assertions.assertTrue(progExercise.getId() > 0L, "progExercise Id must be > 0");
    }

    @Test
    public void ProgExerciseRepository_SaveAll_ReturnSavedProgExercises() {
        List<ProgExerciseEntity> progExercises = createNewTestProgExercises(creator, exercise);

        List<ProgExerciseEntity> savedProgExercises = saveAllProgExercisesInRepository(progExercises);

        Assertions.assertNotNull(savedProgExercises);
        Assertions.assertEquals(progExercises.size(), savedProgExercises.size());
        Assertions.assertTrue(progExercises.getFirst().getId() > 0L, "first user Id must be > 0");
        Assertions.assertTrue(progExercises.getLast().getId() > 1L, "second user Id must be > 0");
    }

    @Test
    public void ProgExerciseRepository_FindById_ReturnProgExercise() {
        ProgExerciseEntity savedProgExercise = saveOneProgExerciseInRepository();

        Optional<ProgExerciseEntity> foundProgExercise = progExerciseRepository.findById(savedProgExercise.getId());

        Assertions.assertNotNull(foundProgExercise);
        Assertions.assertTrue(foundProgExercise.isPresent());
        Assertions.assertNotNull(foundProgExercise.get());
    }

    @Test
    public void ProgExerciseRepository_FindAll_ReturnAllProgExercises() {
        saveAllProgExercisesInRepository(null);

        List<ProgExerciseEntity> progExerciseTypeEntities = StreamSupport.stream(
                        progExerciseRepository.findAll().spliterator(),
                        false)
                .collect(Collectors.toList());

        Assertions.assertNotNull(progExerciseTypeEntities);
        Assertions.assertEquals(progExerciseTypeEntities.size(), 2);
    }

    @Test
    public void ProgExerciseRepository_FindAllById_ReturnAllProgExercises() {
        List<ProgExerciseEntity> progExercises = createNewTestProgExercises(creator, exercise);
        progExerciseRepository.saveAll(progExercises);

        List<ProgExerciseEntity> progExerciseEntities = StreamSupport.stream(
                        progExerciseRepository.findAllById(progExercises.stream()
                                        .map(ProgExerciseEntity::getId)
                                        .toList())
                                .spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(progExerciseEntities);
        Assertions.assertEquals(2, progExerciseEntities.size());
    }

    @Test
    public void ProgExerciseRepository_FindAllBySubscribedUsersId_ReturnAllProgExercisesWithCreatorId() {
        List<ProgExerciseEntity> progExercises = createNewTestProgExercises(creator, exercise);
        progExerciseRepository.saveAll(progExercises);
        creator.getSubscribedProgExercises().add(progExercises.getFirst());
        userRepository.save(creator);

        List<ProgExerciseEntity> progExerciseEntities = progExerciseRepository.findAllBySubscribedUsersId(creator.getId());

        Assertions.assertNotNull(progExerciseEntities);
        Assertions.assertEquals(1, progExerciseEntities.size());
    }

    @Test
    public void ProgExerciseRepository_ExistById_ReturnTrue() {
        ProgExerciseEntity savedProgExercise = saveOneProgExerciseInRepository();

        boolean foundProgExercise = progExerciseRepository.existsById(savedProgExercise.getId());

        Assertions.assertTrue(foundProgExercise);
    }

    @Test
    public void ProgExerciseRepository_Count_ReturnUsersListSize() {
        List<ProgExerciseEntity> savedProgExercises = saveAllProgExercisesInRepository(null);

        Long progExercisesCount = progExerciseRepository.count();

        Assertions.assertEquals(progExercisesCount, savedProgExercises.size());
    }

    @Test
    public void ProgExerciseRepository_Delete_ReturnFalse() {
        ProgExerciseEntity savedProgExercise = saveOneProgExerciseInRepository();

        progExerciseRepository.delete(savedProgExercise);
        boolean foundProgExercise = progExerciseRepository.existsById(savedProgExercise.getId());

        Assertions.assertFalse(foundProgExercise);
    }

    @Test
    public void ProgExerciseRepository_DeleteById_ReturnFalse() {
        ProgExerciseEntity savedProgExercise = saveOneProgExerciseInRepository();

        progExerciseRepository.deleteById(savedProgExercise.getId());
        boolean foundProgExercise = progExerciseRepository.existsById(savedProgExercise.getId());

        Assertions.assertFalse(foundProgExercise);
    }

    @Test
    public void ProgExerciseRepository_DeleteAllById_ReturnAllFalse() {
        List<ProgExerciseEntity> progExerciseEntities = saveAllProgExercisesInRepository(null);

        progExerciseRepository.deleteAllById(progExerciseEntities.stream().map(ProgExerciseEntity::getId).toList());

        progExerciseEntities.forEach(progExercise -> {
            boolean foundProgExercise = progExerciseRepository.existsById(progExercise.getId());
            Assertions.assertFalse(foundProgExercise);
        });
    }

    @Test
    public void ProgExerciseRepository_DeleteAll_ReturnAllFalse() {
        List<ProgExerciseEntity> progExerciseEntities = saveAllProgExercisesInRepository(null);

        progExerciseRepository.deleteAll();

        progExerciseEntities.forEach(progExercise -> {
            boolean foundProgExercise = progExerciseRepository.existsById(progExercise.getId());
            Assertions.assertFalse(foundProgExercise);
        });
    }
}
