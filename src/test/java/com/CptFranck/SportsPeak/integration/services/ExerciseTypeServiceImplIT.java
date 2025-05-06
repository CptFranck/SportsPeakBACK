package com.CptFranck.SportsPeak.integration.services;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseTypeNotFoundException;
import com.CptFranck.SportsPeak.repositories.ExerciseTypeRepository;
import com.CptFranck.SportsPeak.service.impl.ExerciseTypeServiceImpl;
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

import static com.CptFranck.SportsPeak.utils.TestExerciseTypeUtils.createTestExerciseType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class ExerciseTypeServiceImplIT {

    @Autowired
    private ExerciseTypeRepository exerciseTypeRepository;

    @Autowired
    private ExerciseTypeServiceImpl exerciseTypeTypeServiceImpl;

    private ExerciseTypeEntity exerciseType;

    @BeforeEach
    public void setUp() {
        exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));
    }

    @AfterEach
    public void afterEach() {
        this.exerciseTypeRepository.deleteAll();
    }

    @Test
    void findAll_Valid_ReturnListOfExerciseTypeEntity() {
        List<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeTypeServiceImpl.findAll();

        assertEqualExerciseList(List.of(exerciseType), exerciseTypeFound);
    }

    @Test
    void findOne_ExerciseTypeNotFound_ThrowExerciseTypeNotFoundException() {
        Assertions.assertThrows(ExerciseTypeNotFoundException.class, () -> exerciseTypeTypeServiceImpl.findOne(exerciseType.getId() + 1));
    }

    @Test
    void findOne_ValidExerciseTypeId_ReturnExerciseTypeEntity() {
        ExerciseTypeEntity exerciseTypeFound = exerciseTypeTypeServiceImpl.findOne(exerciseType.getId());

        asserEqualExerciseType(exerciseType, exerciseTypeFound);
    }

    @Test
    void findMany_ValidExerciseTypeIds_ReturnSetOfExerciseTypeEntity() {
        Set<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeTypeServiceImpl.findMany(Set.of(exerciseType.getId()));

        assertEqualExerciseList(List.of(exerciseType), exerciseTypeFound.stream().toList());
    }

    @Test
    void create_ValidInputNewExerciseType_ReturnExerciseTypeEntity() {
        ExerciseTypeEntity unsavedExerciseType = createTestExerciseType(null);

        ExerciseTypeEntity exerciseTypeSaved = exerciseTypeTypeServiceImpl.create(unsavedExerciseType);

        asserEqualExerciseType(unsavedExerciseType, exerciseTypeSaved);
    }

    @Test
    void update_ExerciseTypeNotFound_ThrowExerciseTypeNotFoundException() {
        ExerciseTypeEntity unsavedExerciseType = createTestExerciseType(exerciseType.getId() + 1);

        Assertions.assertThrows(ExerciseTypeNotFoundException.class, () -> exerciseTypeTypeServiceImpl.update(unsavedExerciseType));
    }


    @Test
    void update_ValidInputExerciseType_ReturnExerciseTypeEntity() {
        ExerciseTypeEntity unsavedExerciseType = createTestExerciseType(exerciseType.getId());

        ExerciseTypeEntity exerciseTypeSaved = exerciseTypeTypeServiceImpl.update(unsavedExerciseType);

        asserEqualExerciseType(unsavedExerciseType, exerciseTypeSaved);
    }

    @Test
    void delete_ExerciseTypeNotFound_ThrowExerciseTypeNotFoundException() {
        exerciseTypeRepository.delete(exerciseType);

        assertThrows(ExerciseTypeNotFoundException.class, () -> exerciseTypeTypeServiceImpl.delete(exerciseType.getId()));
    }

    @Test
    void delete_ValidInput_Void() {
        assertAll(() -> exerciseTypeTypeServiceImpl.delete(exerciseType.getId()));
    }

    @Test
    void exists_ValidInput_ReturnTrue() {
        boolean exerciseTypeFound = exerciseTypeTypeServiceImpl.exists(exerciseType.getId());

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

    private void asserEqualExerciseType(ExerciseTypeEntity exerciseTypeExpected, ExerciseTypeEntity exerciseTypeObtain) {
        Assertions.assertEquals(exerciseTypeExpected.getId(), exerciseTypeObtain.getId());
        Assertions.assertEquals(exerciseTypeExpected.getName(), exerciseTypeObtain.getName());
        Assertions.assertEquals(exerciseTypeExpected.getGoal(), exerciseTypeObtain.getGoal());
        Assertions.assertEquals(exerciseTypeExpected.getExercises().size(), exerciseTypeObtain.getExercises().size());
    }
}
