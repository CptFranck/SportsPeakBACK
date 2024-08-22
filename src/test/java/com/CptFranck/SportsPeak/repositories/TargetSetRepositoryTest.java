package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
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

import static com.CptFranck.SportsPeak.domain.utils.TestDataExerciseUtils.createNewTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestDataProgExerciseUtils.createTestNewProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestDataTargetSetUtils.createNewTestTargetSets;
import static com.CptFranck.SportsPeak.domain.utils.TestDataUserUtils.createNewTestUser;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TargetSetRepositoryTest {

    private ProgExerciseEntity progExercise;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private TargetSetRepository targetSetRepository;
    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    private List<TargetSetEntity> saveAllTargetSetsInRepository() {
        List<TargetSetEntity> localTargetSets = createNewTestTargetSets(progExercise);
        targetSetRepository.saveAll(localTargetSets);
        return localTargetSets;
    }

    @BeforeEach
    void setUp() {
        UserEntity creator = userRepository.save(createNewTestUser());
        ExerciseEntity exercise = exerciseRepository.save(createNewTestExercise());
        progExercise = progExerciseRepository.save(createTestNewProgExercise(creator, exercise));
    }

    @Test
    public void targetSetRepository_FindAllByProgExerciseId_ReturnTargetSet() {
        saveAllTargetSetsInRepository();

        List<TargetSetEntity> foundTargetSets = targetSetRepository.findAllByProgExerciseId(progExercise.getId());

        Assertions.assertNotNull(foundTargetSets);
        Assertions.assertEquals(2, foundTargetSets.size());
    }

    @Test
    public void targetSetRepository_FindByTargetSetUpdateId_ReturnTargetSet() {
        List<TargetSetEntity> targetSetEntities = saveAllTargetSetsInRepository();
        targetSetEntities.getFirst().setTargetSetUpdate(targetSetEntities.getLast());

        Optional<TargetSetEntity> foundTargetSet = targetSetRepository.findByTargetSetUpdateId(targetSetEntities.getLast().getId());

        Assertions.assertNotNull(foundTargetSet);
        Assertions.assertTrue(foundTargetSet.isPresent());
        Assertions.assertNotNull(foundTargetSet.get());
    }
}
