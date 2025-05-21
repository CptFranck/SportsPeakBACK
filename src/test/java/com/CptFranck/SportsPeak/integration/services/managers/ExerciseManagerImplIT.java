package com.CptFranck.SportsPeak.integration.services.managers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.repository.ExerciseTypeRepository;
import com.CptFranck.SportsPeak.repository.MuscleRepository;
import com.CptFranck.SportsPeak.service.managerImpl.ExerciseManagerImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.CptFranck.SportsPeak.utils.ExerciseTypeTestUtils.asserEqualExerciseType;
import static com.CptFranck.SportsPeak.utils.ExerciseTypeTestUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.utils.MuscleTestUtils.assertEqualMuscle;
import static com.CptFranck.SportsPeak.utils.MuscleTestUtils.createTestMuscle;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class ExerciseManagerImplIT {

    @Autowired
    private ExerciseManagerImpl exerciseManager;

    @Autowired
    private ExerciseTypeRepository exerciseTypeRepository;

    @Autowired
    private MuscleRepository muscleRepository;

    @AfterEach
    public void afterEach() {
        exerciseTypeRepository.deleteAll();
    }

    @Test
    void saveExerciseType_AddNewExerciseType_ReturnExerciseTypeEntity() {
        ExerciseTypeEntity exerciseType = createTestExerciseType(null);

        ExerciseTypeEntity exerciseTypeResolved = exerciseManager.saveExerciseType(exerciseType);

        asserEqualExerciseType(exerciseType, exerciseTypeResolved);
    }

    @Test
    void saveExerciseType_UpdateExerciseType_ReturnExerciseTypeEntity() {
        ExerciseTypeEntity exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));

        ExerciseTypeEntity exerciseTypeResolved = exerciseManager.saveExerciseType(exerciseType);

        asserEqualExerciseType(exerciseType, exerciseTypeResolved);
    }

    @Test
    void saveMuscle_AddNewMuscle_ReturnMuscleEntity() {
        MuscleEntity muscle = createTestMuscle(null);

        MuscleEntity muscleResolved = exerciseManager.saveMuscle(muscle);

        assertEqualMuscle(muscle, muscleResolved);
    }

    @Test
    void saveMuscle_UpdateMuscle_ReturnMuscleEntity() {
        MuscleEntity muscle = muscleRepository.save(createTestMuscle(null));

        MuscleEntity muscleResolved = exerciseManager.saveMuscle(muscle);

        assertEqualMuscle(muscle, muscleResolved);
    }
}
