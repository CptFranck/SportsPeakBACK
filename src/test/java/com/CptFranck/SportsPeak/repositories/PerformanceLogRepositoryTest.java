package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestPerformanceLogUtils.createTestPerformanceLogList;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;

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
        UserEntity creator = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, creator, exercise));
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise));
        performanceLogRepository.saveAll(createTestPerformanceLogList(targetSet));

        List<PerformanceLogEntity> foundPerformanceLogs = performanceLogRepository.findAllByTargetSetId(targetSet.getId());

        Assertions.assertNotNull(foundPerformanceLogs);
        Assertions.assertEquals(2, foundPerformanceLogs.size());
    }
}
