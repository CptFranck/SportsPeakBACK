package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.MuscleService;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseDto;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        ExerciseController.class
})
class ExerciseControllerTest {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @MockBean
    private Mapper<ExerciseEntity, ExerciseDto> exerciseMapper;

    @MockBean
    private MuscleService muscleService;

    @MockBean
    private ExerciseService exerciseService;

    @MockBean
    private ExerciseTypeService exerciseTypeService;

    private ExerciseEntity exercise;

    private List<ExerciseEntity> exercises;

    private ExerciseDto exerciseDto;

    @BeforeEach
    void init() {
        exercise = createTestExercise(1L);
        exercises = List.of(exercise);
        exerciseDto = createTestExerciseDto(1L);
    }

    @Test
    void ExerciseController_GetExercises_Success() {
        when(exerciseService.findAll()).thenReturn(exercises);
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        List<LinkedHashMap<String, Object>> exerciseDtos = dgsQueryExecutor.executeAndExtractJsonPath("query {\n" +
                "    getExercises {" +
                "        id" +
                "        exerciseTypes { id name goal }\n" +
                "        muscles { id name function }\n" +
                "        progExercises { id name note trustLabel visibility} " +
                "        name" +
                "        description" +
                "        goal\n" +
                "    }\n" +
                "}", "data.getExercises");

        Assertions.assertNotNull(exerciseDtos);
    }

    @Test
    void ExerciseController_GetExerciseById_Unsuccessful() {
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());

        LinkedHashMap<String, Object> exerciseDto = dgsQueryExecutor.executeAndExtractJsonPath("query {\n" +
                "    getExerciseById(id: 1) {" +
                "        id" +
                "        exerciseTypes { id name goal }\n" +
                "        muscles { id name function }\n" +
                "        progExercises { id name note trustLabel visibility} " +
                "        name" +
                "        description" +
                "        goal\n" +
                "    }\n" +
                "}", "data.getExerciseById");

        Assertions.assertNull(exerciseDto);
    }

    @Test
    void ExerciseController_GetExerciseById_Success() {
        when(exerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(exercise));
        when(exerciseMapper.mapTo(Mockito.any(ExerciseEntity.class))).thenReturn(exerciseDto);

        LinkedHashMap<String, Object> exerciseDto = dgsQueryExecutor.executeAndExtractJsonPath("query {\n" +
                "    getExerciseById(id: 1) {" +
                "        id" +
                "        exerciseTypes { id name goal }\n" +
                "        muscles { id name function }\n" +
                "        progExercises { id name note trustLabel visibility} " +
                "        name" +
                "        description" +
                "        goal\n" +
                "    }\n" +
                "}", "data.getExerciseById");

        Assertions.assertNotNull(exerciseDto);
    }

//    @Test
//    void addExercise() {
//    }
//
//    @Test
//    void modifyExercise() {
//    }
//
//    @Test
//    void deleteExercise() {
//    }
}