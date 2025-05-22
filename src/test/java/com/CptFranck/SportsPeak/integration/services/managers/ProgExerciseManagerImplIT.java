package com.CptFranck.SportsPeak.integration.services.managers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseStillUsedException;
import com.CptFranck.SportsPeak.domain.exception.tartgetSet.TargetSetNotFoundException;
import com.CptFranck.SportsPeak.repository.ExerciseRepository;
import com.CptFranck.SportsPeak.repository.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repository.TargetSetRepository;
import com.CptFranck.SportsPeak.repository.UserRepository;
import com.CptFranck.SportsPeak.service.managerImpl.ProgExerciseManagerImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;

import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TargetSetTestUtils.assertEqualsTargetSet;
import static com.CptFranck.SportsPeak.utils.TargetSetTestUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUser;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUserBis;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class ProgExerciseManagerImplIT {

    @Autowired
    private ProgExerciseManagerImpl progExerciseManager;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Autowired
    private TargetSetRepository targetSetRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;
    private ProgExerciseEntity progExercise;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
    }

    @AfterEach
    public void AfterEach() {
        userRepository.findAll().forEach(user -> {
            user.setSubscribedProgExercises(new HashSet<>());
            userRepository.save(user);
        });
        targetSetRepository.deleteAll();
        progExerciseRepository.deleteAll();
        exerciseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void saveTargetSet_AddTargetSetWithoutUpdate_ReturnTargetSetEntity() {
        TargetSetEntity targetSet = createTestTargetSet(null, progExercise, null);

        TargetSetEntity targetSetSave = progExerciseManager.saveTargetSet(targetSet, null);

        assertEqualsTargetSet(targetSet, targetSetSave);
    }

    @Test
    void saveTargetSet_AddTargetSetWithUpdate_ReturnTargetSetEntity() {
        TargetSetEntity targetSet = createTestTargetSet(null, progExercise, null);
        TargetSetEntity targetSetBis = targetSetRepository.save(createTestTargetSet(null, progExercise, null));

        TargetSetEntity targetSetSave = progExerciseManager.saveTargetSet(targetSet, targetSetBis.getId());

        targetSetBis = targetSetRepository.findById(targetSetBis.getId()).orElseThrow();
        assertEqualsTargetSet(targetSet, targetSetSave);
        Assertions.assertEquals(targetSetBis.getTargetSetUpdate().getId(), targetSetSave.getId());
    }

    @Test
    void saveTargetSet_InvalidTargetSetId_ThrowTargetSetNotFoundException() {
        TargetSetEntity targetSet = createTestTargetSet(1L, progExercise, null);

        Assertions.assertThrows(TargetSetNotFoundException.class, () -> progExerciseManager.saveTargetSet(targetSet, null));
    }

    @Test
    void deleteProgExercise_UserStillLinkedToProgExercise_ThrowProgExerciseStillUsedException() {
        UserEntity userBis = userRepository.save(createTestUserBis(null));
        user.getSubscribedProgExercises().add(progExercise);
        userBis.getSubscribedProgExercises().add(progExercise);
        userRepository.save(user);
        userRepository.save(userBis);

        assertThrows(ProgExerciseStillUsedException.class, () -> progExerciseManager.deleteProgExercise(progExercise.getId()));
    }

    @Test
    void deleteProgExercise_InvalidProgExerciseId_Void() {
        progExerciseRepository.delete(progExercise);

        Assertions.assertThrows(ProgExerciseNotFoundException.class, () -> progExerciseManager.deleteProgExercise(progExercise.getId()));
    }

    @Test
    void deleteProgExercise_ValidUseWithUserAuthor_UpdatePrivilege_Void() {
        user.getSubscribedProgExercises().add(progExercise);
        userRepository.save(user);

        assertAll(() -> progExerciseManager.deleteProgExercise(progExercise.getId()));
    }
}
