package com.CptFranck.SportsPeak.integration.services.services;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.exception.exerciseType.ExerciseTypeNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.exerciseType.ExerciseTypeStillUsedInExerciseException;
import com.CptFranck.SportsPeak.repository.ExerciseRepository;
import com.CptFranck.SportsPeak.repository.ExerciseTypeRepository;
import com.CptFranck.SportsPeak.service.serviceImpl.ExerciseTypeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.CptFranck.SportsPeak.utils.ExerciseTypeTestUtils.asserEqualExerciseType;
import static com.CptFranck.SportsPeak.utils.ExerciseTypeTestUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.createTestExercise;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class ExerciseTypeServiceImplIT {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseTypeRepository exerciseTypeRepository;

    @Autowired
    private ExerciseTypeServiceImpl exerciseTypeServiceImpl;

    private ExerciseTypeEntity exerciseType;

    @BeforeEach
    public void setUp() {
        exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));
    }

    @AfterEach
    public void afterEach() {
        this.exerciseRepository.deleteAll();
        this.exerciseTypeRepository.deleteAll();
    }

    @Test
    void findAll_ValidUse_ReturnListOfExerciseTypeEntity() {
        List<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeServiceImpl.findAll();

        assertEqualExerciseList(List.of(exerciseType), exerciseTypeFound);
    }

    @Test
    void findOne_InvalidExerciseTypeId_ThrowExerciseTypeNotFoundException() {
        exerciseTypeRepository.delete(exerciseType);

        Assertions.assertThrows(ExerciseTypeNotFoundException.class, () -> exerciseTypeServiceImpl.findOne(exerciseType.getId()));
    }

    @Test
    void findOne_ValidExerciseTypeId_ReturnExerciseTypeEntity() {
        ExerciseTypeEntity exerciseTypeFound = exerciseTypeServiceImpl.findOne(exerciseType.getId());

        asserEqualExerciseType(exerciseType, exerciseTypeFound);
    }

    @Test
    void findMany_ValidExerciseTypeIds_ReturnSetOfExerciseTypeEntity() {
        Set<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeServiceImpl.findMany(Set.of(exerciseType.getId()));

        assertEqualExerciseList(List.of(exerciseType), exerciseTypeFound.stream().toList());
    }

    @Test
    void save_AddNewExerciseType_ReturnExerciseTypeEntity() {
        ExerciseTypeEntity unsavedExerciseType = createTestExerciseType(null);

        ExerciseTypeEntity exerciseTypeSaved = exerciseTypeServiceImpl.save(unsavedExerciseType);

        asserEqualExerciseType(unsavedExerciseType, exerciseTypeSaved);
    }

    @Test
    void save_UpdateExerciseType_ThrowExerciseTypeNotFoundException() {
        exerciseTypeRepository.delete(exerciseType);

        ExerciseTypeEntity unsavedExerciseType = createTestExerciseType(exerciseType.getId());

        Assertions.assertThrows(ExerciseTypeNotFoundException.class, () -> exerciseTypeServiceImpl.save(unsavedExerciseType));
    }


    @Test
    void save_UpdateExerciseType_ReturnExerciseTypeEntity() {
        ExerciseTypeEntity unsavedExerciseType = createTestExerciseType(exerciseType.getId());

        ExerciseTypeEntity exerciseTypeSaved = exerciseTypeServiceImpl.save(unsavedExerciseType);

        asserEqualExerciseType(unsavedExerciseType, exerciseTypeSaved);
    }

    @Test
    void delete_InvalidExerciseTypeId_ThrowExerciseTypeNotFoundException() {
        exerciseTypeRepository.delete(exerciseType);

        assertThrows(ExerciseTypeNotFoundException.class, () -> exerciseTypeServiceImpl.delete(exerciseType.getId()));
    }

    @Test
    void delete_ExerciseTypeStillUsed_ThrowExerciseTypeStillUsedInExerciseException() {
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        exercise.getExerciseTypes().add(exerciseType);
        exerciseRepository.save(exercise);

        assertThrows(ExerciseTypeStillUsedInExerciseException.class, () -> exerciseTypeServiceImpl.delete(exerciseType.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        assertAll(() -> exerciseTypeServiceImpl.delete(exerciseType.getId()));
    }

    @Test
    void exists_ValidInput_ReturnTrue() {
        boolean exerciseTypeFound = exerciseTypeServiceImpl.exists(exerciseType.getId());

        Assertions.assertTrue(exerciseTypeFound);
    }

    private void assertEqualExerciseList(
            List<ExerciseTypeEntity> expectedExerciseTypeList,
            List<ExerciseTypeEntity> exerciseTypeListObtained
    ) {
        Assertions.assertEquals(expectedExerciseTypeList.size(), exerciseTypeListObtained.size());
        expectedExerciseTypeList.forEach(exerciseTypeFound -> asserEqualExerciseType(
                exerciseTypeListObtained.stream().filter(
                        exerciseType -> Objects.equals(exerciseType.getId(), exerciseTypeFound.getId())
                ).toList().getFirst(),
                exerciseTypeFound)
        );
    }
}
