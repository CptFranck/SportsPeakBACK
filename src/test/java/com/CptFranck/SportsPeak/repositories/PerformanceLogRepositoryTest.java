package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.CptFranck.SportsPeak.domain.utils.TestDataPerformanceLogUtils.createNewTestPerformanceLogs;
import static com.CptFranck.SportsPeak.domain.utils.TestDataTargetSetUtils.createNewTestTargetSet;
import static com.CptFranck.SportsPeak.domain.utils.TestDataUserUtils.createNewTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PerformanceLogRepositoryTest {

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
        UserEntity creator = userRepository.save(createNewTestUser());
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(1L));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(1L, creator, exercise));
        TargetSetEntity targetSet = targetSetRepository.save(createNewTestTargetSet(progExercise));
        performanceLogRepository.saveAll(createNewTestPerformanceLogs(targetSet));

        List<PerformanceLogEntity> foundPerformanceLogs = performanceLogRepository.findAllByTargetSetId(targetSet.getId());

        Assertions.assertNotNull(foundPerformanceLogs);
        Assertions.assertEquals(2, foundPerformanceLogs.size());
    }
}
