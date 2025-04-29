package com.CptFranck.SportsPeak.integration.services;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleStillUsedInExerciseException;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.repositories.MuscleRepository;
import com.CptFranck.SportsPeak.service.impl.MuscleServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestMuscleUtils.createTestMuscle;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class MuscleServiceImplIntTest {


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
    void muscleService_Save_Success() {
        MuscleEntity unsavedMuscle = createTestMuscle(null);

        MuscleEntity muscleSaved = muscleServiceImpl.save(unsavedMuscle);

        assertEqualMuscle(unsavedMuscle, muscleSaved);
    }

    @Test
    void muscleService_FindAll_Success() {
        List<MuscleEntity> muscleFound = muscleServiceImpl.findAll();

        assertEqualMuscleList(List.of(muscle), muscleFound);
    }

    @Test
    void muscleService_FindOne_Success() {
        Optional<MuscleEntity> muscleFound = muscleServiceImpl.findOne(muscle.getId());

        Assertions.assertTrue(muscleFound.isPresent());
        assertEqualMuscle(muscle, muscleFound.get());
    }

    @Test
    void muscleService_FindMany_Success() {
        Set<MuscleEntity> muscleFound = muscleServiceImpl.findMany(Set.of(muscle.getId()));

        assertEqualMuscleList(List.of(muscle), muscleFound.stream().toList());
    }

    @Test
    void muscleService_Exists_Success() {
        boolean muscleFound = muscleServiceImpl.exists(muscle.getId());

        Assertions.assertTrue(muscleFound);
    }

    @Test
    void muscleService_Delete_Unsuccessful() {
        muscleRepository.delete(muscle);

        assertThrows(MuscleNotFoundException.class, () -> muscleServiceImpl.delete(muscle.getId()));
    }

    @Test
    void muscleService_Delete_Unsuccessful_Exception() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        exercise.getMuscles().add(muscle);
        exerciseRepository.save(exercise);

        assertThrows(MuscleStillUsedInExerciseException.class, () -> muscleServiceImpl.delete(muscle.getId()));
    }

    @Test
    void muscleService_Delete_Success() {
        assertAll(() -> muscleServiceImpl.delete(muscle.getId()));
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
