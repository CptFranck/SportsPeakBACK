package com.CptFranck.SportsPeak.integration.services.services;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseNotFoundException;
import com.CptFranck.SportsPeak.repository.ExerciseRepository;
import com.CptFranck.SportsPeak.repository.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repository.UserRepository;
import com.CptFranck.SportsPeak.service.serviceImpl.ProgExerciseServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.assertEqualProgExercise;
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class ProgExerciseServiceImplIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Autowired
    private ProgExerciseServiceImpl progExerciseService;

    private UserEntity user;
    private ExerciseEntity exercise;
    private ProgExerciseEntity progExercise;


    @BeforeEach
    void setUp() {
        user = userRepository.save(createTestUser(null));
        exercise = exerciseRepository.save(createTestExercise(null));
        progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
    }

    @AfterEach
    public void afterEach() {
        this.progExerciseRepository.deleteAll();
        this.exerciseRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void findAll_ValidUse_ReturnListOfProgExerciseEntity() {
        List<ProgExerciseEntity> progExerciseFound = progExerciseService.findAll();

        assertEqualProgExerciseList(List.of(progExercise), progExerciseFound);
    }

    @Test
    void findOne_InvalidProgExerciseId_ThrowProgExerciseNotFoundException() {
        progExerciseRepository.delete(progExercise);

        assertThrows(ProgExerciseNotFoundException.class, () -> progExerciseService.findOne(progExercise.getId()));
    }

    @Test
    void findOne_ValidProgExerciseId_ReturnProgExerciseEntity() {
        ProgExerciseEntity progExerciseFound = progExerciseService.findOne(progExercise.getId());

        assertEqualProgExercise(progExercise, progExerciseFound);
    }

    @Test
    void findMany_ValidProgExerciseIds_ReturnSetOfProgExerciseEntity() {
        Set<ProgExerciseEntity> progExerciseFound = progExerciseService.findMany(Set.of(progExercise.getId()));

        assertEqualProgExerciseList(List.of(progExercise), progExerciseFound.stream().toList());
    }

    @Test
    void save_AddProgExercise_ReturnProgExerciseEntity() {
        ProgExerciseEntity unsavedProgExercise = createTestProgExercise(null, user, exercise);

        ProgExerciseEntity progExerciseSaved = progExerciseService.save(unsavedProgExercise);

        assertEqualProgExercise(unsavedProgExercise, progExerciseSaved);
    }

    @Test
    void save_UpdateProgExerciseNotExisting_ThrowProgExerciseNotFoundException() {
        progExerciseRepository.delete(progExercise);

        assertThrows(ProgExerciseNotFoundException.class, () -> progExerciseService.save(progExercise));
    }

    @Test
    void save_UpdateProgExercise_ReturnProgExerciseEntity() {
        ProgExerciseEntity progExerciseSaved = progExerciseService.save(progExercise);

        assertEqualProgExercise(progExercise, progExerciseSaved);
    }

    @Test
    void delete_InvalidProgExerciseId_ThrowProgExerciseNotFoundException() {
        progExerciseRepository.delete(progExercise);

        assertThrows(ProgExerciseNotFoundException.class, () -> progExerciseService.delete(progExercise.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        assertAll(() -> progExerciseService.delete(progExercise.getId()));
    }

    @Test
    void exists_ValidInput_ReturnTrue() {
        boolean progExerciseFound = progExerciseService.exists(progExercise.getId());

        Assertions.assertTrue(progExerciseFound);
    }

    private void assertEqualProgExerciseList(
            List<ProgExerciseEntity> expectedProgExerciseList,
            List<ProgExerciseEntity> progExerciseListObtained
    ) {
        Assertions.assertEquals(expectedProgExerciseList.size(), progExerciseListObtained.size());
        expectedProgExerciseList.forEach(progExerciseFound -> assertEqualProgExercise(
                progExerciseListObtained.stream().filter(
                        progExercise -> Objects.equals(progExercise.getId(), progExerciseFound.getId())
                ).toList().getFirst(),
                progExerciseFound)
        );
    }
}
