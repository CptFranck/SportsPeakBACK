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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseList;
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
    private MuscleService muscleService;
    @MockBean
    private ExerciseService exerciseService;
    @MockBean
    private ExerciseTypeService exerciseTypeService;
    @MockBean
    private Mapper<ExerciseEntity, ExerciseDto> exerciseMapper;

    private List<ExerciseEntity> exerciseEntities;

    @BeforeEach
    void init() {
        exerciseEntities = createTestExerciseList();
    }

    @Test
    void getExercises() {

        when(exerciseService.findAll()).thenReturn(exerciseEntities);

        List<ExerciseDto> exerciseDtos = dgsQueryExecutor.executeAndExtractJsonPath("query {\n" +
                "    getExercises {\n" +
                "        id\n" +
                "        exerciseTypes {\n" +
                "            id\n" +
                "            name\n" +
                "            goal\n" +
                "        }\n" +
                "        muscles {\n" +
                "            id\n" +
                "            name\n" +
                "            function\n" +
                "        }\n" +
                "        progExercises {\n" +
                "            id\n" +
                "            name\n" +
                "            note\n" +
                "            trustLabel\n" +
                "            visibility\n" +
                "        }\n" +
                "        name\n" +
                "        description\n" +
                "        goal\n" +
                "    }\n" +
                "}", "data.getExercises[*].id");

        Assertions.assertNotNull(exerciseDtos);
    }

//    @Test
//    void getExerciseById() {
//    }
//
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