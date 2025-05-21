package com.CptFranck.SportsPeak.integration.repositories;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.repository.ExerciseRepository;
import com.CptFranck.SportsPeak.repository.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repository.TargetSetRepository;
import com.CptFranck.SportsPeak.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.createTestTargetSetList;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;

@DataJpaTest
public class TargetSetRepositoryIntTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private TargetSetRepository targetSetRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    private ProgExerciseEntity progExercise;

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

    @AfterEach
    public void afterEach() {
        this.targetSetRepository.deleteAll();
        this.progExerciseRepository.deleteAll();
        this.exerciseRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    public void targetSetRepository_FindAllByProgExerciseId_ReturnTargetSet() {
        List<TargetSetEntity> targetSetEntities = saveAllTargetSetsInRepository();

        List<TargetSetEntity> foundTargetSets = targetSetRepository.findAllByProgExerciseId(progExercise.getId());

        Assertions.assertNotNull(foundTargetSets);
        Assertions.assertEquals(targetSetEntities.size(), foundTargetSets.size());
    }

    @Test
    public void targetSetRepository_FindByTargetSetUpdateId_ReturnTargetSet() {
        List<TargetSetEntity> targetSetEntities = saveAllTargetSetsInRepository();
        TargetSetEntity targetSet1 = targetSetEntities.getFirst();
        TargetSetEntity targetSet2 = targetSetEntities.getLast();
        targetSet1.setTargetSetUpdate(targetSet2);
        targetSetRepository.save(targetSet1);

        Optional<TargetSetEntity> foundTargetSet = targetSetRepository.findByTargetSetUpdateId(targetSet2.getId());

        Assertions.assertNotNull(foundTargetSet);
        Assertions.assertTrue(foundTargetSet.isPresent());
        Assertions.assertEquals(foundTargetSet.get().getId(), targetSet1.getId());
    }
}
