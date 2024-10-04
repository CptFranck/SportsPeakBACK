package com.CptFranck.SportsPeak.repositories.IntegrationTest;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.repositories.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repositories.TargetSetRepository;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSetList;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;

@DataJpaTest
public class TargetSetRepositoryIntTest {

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
        List<TargetSetEntity> localTargetSets = createTestTargetSetList(false, progExercise);
        targetSetRepository.saveAll(localTargetSets);
        return localTargetSets;
    }

    @BeforeEach
    void setUp() {
        UserEntity creator = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        progExercise = progExerciseRepository.save(createTestProgExercise(null, creator, exercise));
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
        targetSetRepository.save(targetSetEntities.getFirst());

        Optional<TargetSetEntity> foundTargetSet = targetSetRepository.findByTargetSetUpdateId(targetSetEntities.getLast().getId());

        Assertions.assertNotNull(foundTargetSet);
        Assertions.assertTrue(foundTargetSet.isPresent());
        Assertions.assertNotNull(foundTargetSet.get());
    }
}
