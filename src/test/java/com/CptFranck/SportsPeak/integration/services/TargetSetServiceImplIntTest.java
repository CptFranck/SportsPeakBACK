package com.CptFranck.SportsPeak.integration.services;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.enumType.TargetSetState;
import com.CptFranck.SportsPeak.domain.exception.tartgetSet.TargetSetNotFoundException;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.repositories.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repositories.TargetSetRepository;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import com.CptFranck.SportsPeak.service.impl.TargetSetServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import static com.CptFranck.SportsPeak.domain.utils.TestDateTimeUtils.assertDatetimeWithTimestamp;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSetList;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class TargetSetServiceImplIntTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Autowired
    private TargetSetRepository targetSetRepository;

    @Autowired
    private TargetSetServiceImpl targetSetServiceImpl;

    private ProgExerciseEntity progExercise;


    @BeforeEach
    void setUp() {
        UserEntity user = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
    }

    @AfterEach
    public void afterEach() {
        this.targetSetRepository.deleteAll();
        this.progExerciseRepository.deleteAll();
        this.exerciseRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void TargetSetService_Save_Success() {
        TargetSetEntity unsavedTargetSet = createTestTargetSet(null, progExercise, null);

        TargetSetEntity targetSetSaved = targetSetServiceImpl.save(unsavedTargetSet);

        assertEqualsTargetSet(unsavedTargetSet, targetSetSaved);
    }

    @Test
    void TargetSetService_FindAll_Success() {
        List<TargetSetEntity> targetSetList = StreamSupport.stream(
                targetSetRepository.saveAll(createTestTargetSetList(true, progExercise)).spliterator(),
                false).toList();

        List<TargetSetEntity> targetSetFound = targetSetServiceImpl.findAll();

        assertEqualTargetSetList(targetSetList, targetSetFound);
    }

    @Test
    void TargetSetService_FindOne_Success() {
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));

        Optional<TargetSetEntity> targetSetFound = targetSetServiceImpl.findOne(targetSet.getId());

        Assertions.assertTrue(targetSetFound.isPresent());
        assertEqualsTargetSet(targetSet, targetSetFound.get());
    }

    @Test
    void TargetSetService_FindMany_Success() {
        List<TargetSetEntity> targetSetList = StreamSupport.stream(
                targetSetRepository.saveAll(createTestTargetSetList(true, progExercise)).spliterator(),
                false).toList();
        Set<Long> targetSetIds = targetSetList.stream().map(TargetSetEntity::getId).collect(Collectors.toSet());

        Set<TargetSetEntity> targetSetFound = targetSetServiceImpl.findMany(targetSetIds);

        assertEqualTargetSetList(targetSetList, targetSetFound.stream().toList());
    }

    @Test
    void TargetSetService_FindAllByProgExerciseId_Success() {
        List<TargetSetEntity> targetSetList = StreamSupport.stream(
                targetSetRepository.saveAll(createTestTargetSetList(true, progExercise)).spliterator(),
                false).toList();

        List<TargetSetEntity> targetSetFound = targetSetServiceImpl.findAllByProgExerciseId(progExercise.getId());
        assertEqualTargetSetList(targetSetList, targetSetFound);
    }

    @Test
    void TargetSetService_UpdatePreviousUpdateState_Success() {
        TargetSetEntity targetSetUpdate = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, targetSetUpdate));

        targetSetServiceImpl.updatePreviousUpdateState(targetSetUpdate.getId(), TargetSetState.HIDDEN);

        Optional<TargetSetEntity> targetSetUpdated = targetSetServiceImpl.findOne(targetSet.getId());
        Assertions.assertTrue(targetSetUpdated.isPresent());
        Assertions.assertEquals(TargetSetState.HIDDEN, targetSetUpdated.get().getState());
    }

    @Test
    void TargetSetService_UpdatePreviousUpdateState_TargetSetNotFoundSuccess() {
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));

        assertAll(() -> targetSetServiceImpl.updatePreviousUpdateState(targetSet.getId(), TargetSetState.HIDDEN));
    }

    @Test
    void TargetSetService_Exists_Success() {
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));

        boolean targetSetFound = targetSetServiceImpl.exists(targetSet.getId());

        Assertions.assertTrue(targetSetFound);
    }

    @Test
    void TargetSetService_Delete_Success() {
        TargetSetEntity targetSet1 = targetSetServiceImpl.save(createTestTargetSet(null, progExercise, null));
        TargetSetEntity targetSet2 = targetSetServiceImpl.save(createTestTargetSet(null, progExercise, targetSet1));
        TargetSetEntity targetSet3 = targetSetServiceImpl.save(createTestTargetSet(null, progExercise, targetSet2));
        targetSetServiceImpl.delete(targetSet2.getId());

        boolean targetSetFound = targetSetServiceImpl.exists(targetSet2.getId());

        targetSet1 = targetSetServiceImpl.findOne(targetSet1.getId()).orElseThrow();
        targetSet3 = targetSetServiceImpl.findOne(targetSet3.getId()).orElseThrow();
        Assertions.assertFalse(targetSetFound);
        Assertions.assertEquals(targetSet3.getTargetSetUpdate().getId(), targetSet1.getId());
    }

    @Test
    void TargetSetService_Delete_Unsuccessful() {
        TargetSetEntity targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
        targetSetRepository.delete(targetSet);

        assertThrows(TargetSetNotFoundException.class, () -> targetSetServiceImpl.delete(targetSet.getId()));
    }

    private void assertEqualTargetSetList(
            List<TargetSetEntity> expectedTargetSetList,
            List<TargetSetEntity> targetSetListObtained
    ) {
        expectedTargetSetList.forEach(exerciseFound -> assertEqualsTargetSet(
                targetSetListObtained.stream().filter(
                        exercise -> Objects.equals(exercise.getId(), exerciseFound.getId())
                ).toList().getFirst(),
                exerciseFound)
        );
    }

    private void assertEqualsTargetSet(TargetSetEntity expected, TargetSetEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getIndex(), actual.getIndex());
        Assertions.assertEquals(expected.getSetNumber(), actual.getSetNumber());
        Assertions.assertEquals(expected.getRepetitionNumber(), actual.getRepetitionNumber());
        Assertions.assertEquals(expected.getWeight(), actual.getWeight());
        Assertions.assertEquals(expected.getWeightUnit(), actual.getWeightUnit());
        Assertions.assertEquals(expected.getPhysicalExertionUnitTime(), actual.getPhysicalExertionUnitTime());
        Assertions.assertEquals(expected.getRestTime(), actual.getRestTime());
        assertDatetimeWithTimestamp(expected.getCreationDate(), actual.getCreationDate());
    }
}
