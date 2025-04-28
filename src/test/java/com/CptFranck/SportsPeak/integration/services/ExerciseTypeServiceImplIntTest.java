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
import java.util.Optional;
import java.util.Set;

import static com.CptFranck.SportsPeak.utils.TestExerciseTypeUtils.createTestExerciseType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class ExerciseTypeServiceImplIntTest {

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
    void exerciseTypeService_Save_Success() {
        ExerciseTypeEntity unsavedExerciseType = createTestExerciseType(null);

        ExerciseTypeEntity exerciseTypeSaved = exerciseTypeTypeServiceImpl.save(unsavedExerciseType);

        asserEqualExerciseType(unsavedExerciseType, exerciseTypeSaved);
    }

    @Test
    void exerciseTypeService_FindAll_Success() {
        List<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeTypeServiceImpl.findAll();

        assertEqualExerciseList(List.of(exerciseType), exerciseTypeFound);
    }

    @Test
    void exerciseTypeService_FindOne_Success() {
        Optional<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeTypeServiceImpl.findOne(exerciseType.getId());

        Assertions.assertTrue(exerciseTypeFound.isPresent());
        asserEqualExerciseType(exerciseType, exerciseTypeFound.get());
    }

    @Test
    void exerciseTypeService_FindMany_Success() {
        Set<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeTypeServiceImpl.findMany(Set.of(exerciseType.getId()));

        assertEqualExerciseList(List.of(exerciseType), exerciseTypeFound.stream().toList());
    }

    @Test
    void exerciseTypeService_Exists_Success() {
        boolean exerciseTypeFound = exerciseTypeTypeServiceImpl.exists(exerciseType.getId());

        Assertions.assertTrue(exerciseTypeFound);
    }

    @Test
    void exerciseTypeService_Delete_Success() {
        assertAll(() -> exerciseTypeTypeServiceImpl.delete(exerciseType.getId()));
    }

    @Test
    void exerciseTypeService_Delete_Unsuccessful() {
        exerciseTypeRepository.delete(exerciseType);

        assertThrows(ExerciseTypeNotFoundException.class, () -> exerciseTypeTypeServiceImpl.delete(exerciseType.getId()));
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
    }
}
