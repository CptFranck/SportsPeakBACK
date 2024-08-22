package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PerformanceLogRepositoryTest {

    private TargetSetEntity targetSet;

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

    @BeforeEach
    void setUp() {
        UserEntity creator = userRepository.save(createNewTestUser(0));
        ExerciseEntity exercise = exerciseRepository.save(createNewTestExercise());
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestNewProgExercise(creator, exercise));
        targetSet = targetSetRepository.save(createNewTestTargetSet(progExercise));
    }

    @Test
    public void PerformanceLogRepository_FindAllByTargetSetId_ReturnSavedExercise() {
        performanceLogRepository.saveAll(createNewTestPerformanceLogs(targetSet));

        List<PerformanceLogEntity> foundPerformanceLogs = performanceLogRepository.findAllByTargetSetId(targetSet.getId());

        Assertions.assertNotNull(foundPerformanceLogs);
        Assertions.assertEquals(2, foundPerformanceLogs.size());
    }
}
