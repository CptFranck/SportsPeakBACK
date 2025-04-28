package com.CptFranck.SportsPeak.integration.services;

import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseTypeNotFoundException;
import com.CptFranck.SportsPeak.repositories.ExerciseTypeRepository;
import com.CptFranck.SportsPeak.service.impl.ExerciseTypeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseTypeUtils.createTestExerciseType;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseTypeUtils.createTestExerciseTypeList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class ExerciseTypeServiceImplIntTest {

    @Autowired
    private ExerciseTypeRepository exerciseTypeRepository;

    @Autowired
    private ExerciseTypeServiceImpl exerciseTypeTypeServiceImpl;


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
        List<ExerciseTypeEntity> exerciseTypeList = StreamSupport.stream(
                exerciseTypeRepository.saveAll(createTestExerciseTypeList(true)).spliterator(),
                false).toList();
        ;

        List<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeTypeServiceImpl.findAll();

        assertEqualExerciseList(exerciseTypeList, exerciseTypeFound);
    }

    @Test
    void exerciseTypeService_FindOne_Success() {
        ExerciseTypeEntity exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));

        Optional<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeTypeServiceImpl.findOne(exerciseType.getId());

        Assertions.assertTrue(exerciseTypeFound.isPresent());
        asserEqualExerciseType(exerciseType, exerciseTypeFound.get());
    }

    @Test
    void exerciseTypeService_FindMany_Success() {
        List<ExerciseTypeEntity> exerciseTypeList = StreamSupport.stream(
                exerciseTypeRepository.saveAll(createTestExerciseTypeList(true)).spliterator(),
                false).toList();
        Set<Long> exerciseTypeIds = exerciseTypeList.stream().map(ExerciseTypeEntity::getId).collect(Collectors.toSet());

        Set<ExerciseTypeEntity> exerciseTypeFound = exerciseTypeTypeServiceImpl.findMany(exerciseTypeIds);

        assertEqualExerciseList(exerciseTypeList, exerciseTypeFound.stream().toList());
    }

    @Test
    void exerciseTypeService_Exists_Success() {
        ExerciseTypeEntity exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));

        boolean exerciseTypeFound = exerciseTypeTypeServiceImpl.exists(exerciseType.getId());

        Assertions.assertTrue(exerciseTypeFound);
    }

    @Test
    void exerciseTypeService_Delete_Success() {
        ExerciseTypeEntity exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));

        assertAll(() -> exerciseTypeTypeServiceImpl.delete(exerciseType.getId()));
    }

    @Test
    void exerciseTypeService_Delete_Unsuccessful() {
        ExerciseTypeEntity exerciseType = exerciseTypeRepository.save(createTestExerciseType(null));
        exerciseTypeRepository.delete(exerciseType);

        assertThrows(ExerciseTypeNotFoundException.class, () -> exerciseTypeTypeServiceImpl.delete(exerciseType.getId()));
    }

    private void assertEqualExerciseList(
            List<ExerciseTypeEntity> expectedExerciseTypeList,
            List<ExerciseTypeEntity> exerciseTypeListObtained
    ) {
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
