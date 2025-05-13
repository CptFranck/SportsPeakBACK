package com.CptFranck.SportsPeak.integration.services;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleStillUsedInExerciseException;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.repositories.MuscleRepository;
import com.CptFranck.SportsPeak.service.serviceImpl.MuscleServiceImpl;
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

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestMuscleUtils.createTestMuscle;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class MuscleServiceImplIT {

    @Autowired
    private MuscleRepository muscleRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private MuscleServiceImpl muscleServiceImpl;

    private MuscleEntity muscle;

    @BeforeEach
    public void setUp() {
        muscle = muscleRepository.save(createTestMuscle(null));
    }

    @AfterEach
    public void afterEach() {
        exerciseRepository.deleteAll();
        muscleRepository.deleteAll();
    }

    @Test
    void findAll_ValidUse_ReturnListOfMuscleEntity() {
        List<MuscleEntity> muscleFound = muscleServiceImpl.findAll();

        assertEqualMuscleList(List.of(muscle), muscleFound);
    }

    @Test
    void findOne_InvalidMuscleId_ThrowMuscleNotFoundException() {
        muscleRepository.delete(muscle);

        assertThrows(MuscleNotFoundException.class, () -> muscleServiceImpl.findOne(muscle.getId()));
    }

    @Test
    void findOne_ValidMuscleId_ReturnMuscleEntity() {
        MuscleEntity muscleFound = muscleServiceImpl.findOne(muscle.getId());

        assertEqualMuscle(muscle, muscleFound);
    }

    @Test
    void findMany_ValidMuscleIds_ReturnSetOfMuscleEntity() {
        Set<MuscleEntity> muscleFound = muscleServiceImpl.findMany(Set.of(muscle.getId()));

        assertEqualMuscleList(List.of(muscle), muscleFound.stream().toList());
    }

    @Test
    void save_AddNewMuscle_ReturnMuscleEntity() {
        MuscleEntity unsavedMuscle = createTestMuscle(null);

        MuscleEntity muscleSaved = muscleServiceImpl.save(unsavedMuscle);

        assertEqualMuscle(unsavedMuscle, muscleSaved);
    }

    @Test
    void save_UpdateMuscleWithInvalidId_ThrowMuscleNotFoundException() {
        MuscleEntity unsavedMuscle = createTestMuscle(muscle.getId());
        muscleRepository.delete(muscle);
        Assertions.assertThrows(MuscleNotFoundException.class, () -> muscleServiceImpl.save(unsavedMuscle));
    }

    @Test
    void save_UpdateMuscle_ReturnMuscleEntity() {
        MuscleEntity muscleSaved = muscleServiceImpl.save(muscle);

        assertEqualMuscle(muscle, muscleSaved);
    }

    @Test
    void delete_InvalidMuscleTypeId_ThrowMuscleTypeNotFoundException() {
        muscleRepository.delete(muscle);

        assertThrows(MuscleNotFoundException.class, () -> muscleServiceImpl.delete(muscle.getId()));
    }

    @Test
    void delete_MuscleStillUsedByExercise_ThrowMuscleStillUsedInExerciseException() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        exercise.getMuscles().add(muscle);
        exerciseRepository.save(exercise);

        assertThrows(MuscleStillUsedInExerciseException.class, () -> muscleServiceImpl.delete(muscle.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        assertAll(() -> muscleServiceImpl.delete(muscle.getId()));
    }

    @Test
    void exists_ValidInput_ReturnTrue() {
        boolean muscleFound = muscleServiceImpl.exists(muscle.getId());

        Assertions.assertTrue(muscleFound);
    }

    private void assertEqualMuscleList(
            List<MuscleEntity> expectedMuscleList,
            List<MuscleEntity> muscleListObtained
    ) {
        Assertions.assertEquals(expectedMuscleList.size(), muscleListObtained.size());
        expectedMuscleList.forEach(muscleFound -> assertEqualMuscle(
                muscleListObtained.stream().filter(
                        muscle -> Objects.equals(muscle.getId(), muscleFound.getId())
                ).toList().getFirst(),
                muscleFound)
        );
    }

    private void assertEqualMuscle(MuscleEntity expected, MuscleEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getLatinName(), actual.getLatinName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getFunction(), actual.getFunction());
        Assertions.assertEquals(expected.getExercises().size(), actual.getExercises().size());
    }
}
