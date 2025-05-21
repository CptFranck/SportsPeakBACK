package com.CptFranck.SportsPeak.integration.repositories;

import com.CptFranck.SportsPeak.domain.entity.*;
import com.CptFranck.SportsPeak.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestPerformanceLogUtils.createNewTestPerformanceLogList;
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TargetSetTestUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;

@DataJpaTest
public class PerformanceLogRepositoryIntTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private TargetSetRepository targetSetRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Autowired
    private PerformanceLogRepository performanceLogRepository;

    @Test
    public void performanceLogRepository_FindAllByTargetSetId_ReturnSavedExercise() {
        UserEntity creator = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, creator, exercise));
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));

        performanceLogRepository.saveAll(createNewTestPerformanceLogList(targetSet));

        List<PerformanceLogEntity> foundPerformanceLogs = performanceLogRepository.findAllByTargetSetId(targetSet.getId());

        Assertions.assertNotNull(foundPerformanceLogs);
        Assertions.assertEquals(2, foundPerformanceLogs.size());
    }
}
