package com.CptFranck.SportsPeak.service.IntegrationTest;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.muscle.MuscleStillUsedInExerciseException;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.repositories.MuscleRepository;
import com.CptFranck.SportsPeak.service.impl.MuscleServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.createTestMuscle;
import static com.CptFranck.SportsPeak.domain.utils.TestMuscleUtils.createTestMuscleList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class MuscleServiceImplIntTest {


    @Autowired
    private MuscleRepository muscleRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private MuscleServiceImpl muscleServiceImpl;

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
        List<MuscleEntity> muscleList = StreamSupport.stream(
                muscleRepository.saveAll(createTestMuscleList(true)).spliterator(),
                false).toList();

        List<MuscleEntity> muscleFound = muscleServiceImpl.findAll();

        assertEqualMuscleList(muscleList, muscleFound);
    }

    @Test
    void muscleService_FindOne_Success() {
        MuscleEntity muscle = muscleRepository.save(createTestMuscle(null));


        Optional<MuscleEntity> muscleFound = muscleServiceImpl.findOne(muscle.getId());

        Assertions.assertTrue(muscleFound.isPresent());
        assertEqualMuscle(muscle, muscleFound.get());
    }

    @Test
    void muscleService_FindMany_Success() {
        List<MuscleEntity> muscleList = StreamSupport.stream(
                muscleRepository.saveAll(createTestMuscleList(true)).spliterator(),
                false).toList();
        Set<Long> muscleIds = muscleList.stream().map(MuscleEntity::getId).collect(Collectors.toSet());

        Set<MuscleEntity> muscleFound = muscleServiceImpl.findMany(muscleIds);

        assertEqualMuscleList(muscleList, muscleFound.stream().toList());
    }

    @Test
    void muscleService_Exists_Success() {
        MuscleEntity muscle = muscleRepository.save(createTestMuscle(null));

        boolean muscleFound = muscleServiceImpl.exists(muscle.getId());

        Assertions.assertTrue(muscleFound);
    }

    @Test
    void muscleService_Delete_Unsuccessful() {
        MuscleEntity muscle = muscleRepository.save(createTestMuscle(null));
        muscleRepository.delete(muscle);

        assertThrows(MuscleNotFoundException.class, () -> muscleServiceImpl.delete(muscle.getId()));
    }

    @Test
    void muscleService_Delete_Unsuccessful_Exception() {
        MuscleEntity muscle = muscleRepository.save(createTestMuscle(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        exercise.getMuscles().add(muscle);
        exerciseRepository.save(exercise);

        assertThrows(MuscleStillUsedInExerciseException.class, () -> muscleServiceImpl.delete(muscle.getId()));
    }

    @Test
    void muscleService_Delete_Success() {
        MuscleEntity muscle = muscleRepository.save(createTestMuscle(null));

        assertAll(() -> muscleServiceImpl.delete(muscle.getId()));
    }

    private void assertEqualMuscleList(
            List<MuscleEntity> expectedMuscleList,
            List<MuscleEntity> muscleListObtained
    ) {
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
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getFunction(), actual.getFunction());
    }
}
