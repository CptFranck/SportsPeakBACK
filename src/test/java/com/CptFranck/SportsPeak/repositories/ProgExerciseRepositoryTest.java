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

    @BeforeEach
    public void init() {
        creator = userRepository.save(createNewTestUser());
        exercise = exerciseRepository.save(createNewTestExercise());
    }

    @Test
    public void ProgExerciseRepository_Save_ReturnSavedProgExercise() {
        ProgExerciseEntity progExercise = createTestNewProgExercise(creator, exercise);

        ProgExerciseEntity savedExercise = progExerciseRepository.save(progExercise);

        Assertions.assertNotNull(savedExercise);
        Assertions.assertTrue(progExercise.getId() > 0L, "progExercise Id must be > 0");
    }

    @Test
    public void ProgExerciseRepository_FindAll_ReturnAllProgExercise() {
        ProgExerciseEntity progExerciseOne = createTestNewProgExercise(this.creator, this.exercise);
        ProgExerciseEntity progExerciseTwo = createTestNewProgExercise(this.creator, this.exercise);
        progExerciseRepository.save(progExerciseOne);
        progExerciseRepository.save(progExerciseTwo);

        List<ProgExerciseEntity> progExerciseTypeEntities = StreamSupport.stream(progExerciseRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());

        Assertions.assertNotNull(progExerciseTypeEntities);
        Assertions.assertEquals(progExerciseTypeEntities.size(), 2);
    }

    @Test
    public void ProgExerciseRepository_FindById_ReturnProgExercise() {
        ProgExerciseEntity progExercise = createTestNewProgExercise(this.creator, this.exercise);
        ProgExerciseEntity savedExercise = progExerciseRepository.save(progExercise);

        Optional<ProgExerciseEntity> foundExercise = progExerciseRepository.findById(savedExercise.getId());

        Assertions.assertNotNull(foundExercise);
        Assertions.assertTrue(foundExercise.isPresent());
        Assertions.assertNotNull(foundExercise.get());
    }

    @Test
    public void ProgExerciseRepository_ExistById_ReturnTrue() {
        ProgExerciseEntity progExercise = createTestNewProgExercise(this.creator, this.exercise);
        ProgExerciseEntity savedProgExercise = progExerciseRepository.save(progExercise);

        boolean foundExercise = progExerciseRepository.existsById(savedProgExercise.getId());

        Assertions.assertTrue(foundExercise);
    }

    @Test
    public void ProgExerciseRepository_DeleteById_ReturnTrue() {
        ProgExerciseEntity progExercise = createTestNewProgExercise(this.creator, this.exercise);
        ProgExerciseEntity savedExercise = progExerciseRepository.save(progExercise);

        progExerciseRepository.deleteById(savedExercise.getId());
        boolean foundExercise = progExerciseRepository.existsById(savedExercise.getId());

        Assertions.assertFalse(foundExercise);
    }
}
