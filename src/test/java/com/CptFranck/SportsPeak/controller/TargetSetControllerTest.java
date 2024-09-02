package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.config.graphql.LocalDateTimeScalar;
import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.dto.UserDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import com.CptFranck.SportsPeak.service.TargetSetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import org.intellij.lang.annotations.Language;
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

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExerciseDto;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSet;
import static com.CptFranck.SportsPeak.domain.utils.TestTargetSetUtils.createTestTargetSetDto;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUserDto;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        DgsAutoConfiguration.class,
        LocalDateTimeScalar.class,
        TargetSetController.class
})
class TargetSetControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;
    @MockBean
    private Mapper<TargetSetEntity, TargetSetDto> TargetSetMapper;

    @MockBean
    private TargetSetService targetSetService;

    @MockBean
    private ProgExerciseService progExerciseService;

    private TargetSetEntity targetSet;
    private TargetSetDto targetSetDto;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        UserEntity user = createTestUser(1L);
        UserDto userDto = createTestUserDto();
        ExerciseEntity exercise = createTestExercise(1L);
        ExerciseDto exerciseDto = createTestExerciseDto(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(1L, user, exercise);
        ProgExerciseDto progExerciseDto = createTestProgExerciseDto(userDto, exerciseDto);
        targetSet = createTestTargetSet(1L, progExercise, null);
        targetSetDto = createTestTargetSetDto(progExerciseDto, null);
        variables = new LinkedHashMap<>();
    }

    @Test
    void TargetSetController_GetTargetSets_Success() {
        when(targetSetService.findAll()).thenReturn(List.of(targetSet));
        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(targetSetDto);

        @Language("GraphQL")
        String query = """
                 query {
                     getTargetSets {
                         id
                         index
                         setNumber
                         repetitionNumber
                         weight
                         weightUnit
                         physicalExertionUnitTime{
                             hours
                             minutes
                             seconds
                         }
                         restTime{
                             hours
                             minutes
                             seconds
                         }
                         creationDate
                         state
                         targetSetUpdate {
                             id
                         }
                         performanceLogs {
                             id
                             setIndex
                             repetitionNumber
                             weight
                             weightUnit
                             logDate
                         }
                         progExercise {
                             id
                             name
                             note
                             exercise {
                                 id
                                 name
                             }
                             trustLabel
                             visibility
                             creator {
                                 id
                                 username
                             }
                         }
                     }
                 }
                """;

        List<LinkedHashMap<String, Object>> TargetSetDtos =
                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getTargetSets");

        Assertions.assertNotNull(TargetSetDtos);
    }

//    @Test
//    void TargetSetController_GetTargetSetById_Unsuccessful() {
//        variables.put("id", 1);
//        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());
//
//        @Language("GraphQL")
//        String query = """
//                 query ($id : Int!){
//                       getTargetSetById(id: $id) {
//                           id
//                           setIndex
//                           repetitionNumber
//                           weight
//                           weightUnit
//                           logDate
//                           targetSet {
//                               id
//                           }
//                       }
//                   }
//                """;
//
//        LinkedHashMap<String, Object> TargetSetDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getTargetSetById", variables);
//
//        Assertions.assertNull(TargetSetDto);
//    }
//
//    @Test
//    void TargetSetController_GetTargetSetById_Success() {
//        variables.put("id", 1);
//        when(targetSetService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(performanceLog));
//        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(performanceLogDto);
//
//        @Language("GraphQL")
//        String query = """
//                 query ($id : Int!){
//                     getTargetSetById(id: $id) {
//                         id
//                         setIndex
//                         repetitionNumber
//                         weight
//                         weightUnit
//                         logDate
//                         targetSet {
//                             id
//                         }
//                     }
//                 }
//                """;
//
//        LinkedHashMap<String, Object> TargetSetDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getTargetSetById", variables);
//
//        Assertions.assertNotNull(TargetSetDto);
//    }
//
//    @Test
//    void TargetSetController_GetTargetSetsByTargetId_Success() {
//        variables.put("targetSetId", 1);
//        when(targetSetService.findAllByTargetSetId(Mockito.any(Long.class))).thenReturn(List.of(performanceLog));
//        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(performanceLogDto);
//
//        @Language("GraphQL")
//        String query = """
//                 query ($targetSetId : Int!){
//                       getTargetSetsByTargetSetsId(targetSetId: $targetSetId) {
//                           id
//                           setIndex
//                           repetitionNumber
//                           weight
//                           weightUnit
//                           logDate
//                           targetSet {
//                               id
//                           }
//                       }
//                   }
//                """;
//
//        List<LinkedHashMap<String, Object>> TargetSetDtos =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.getTargetSetsByTargetSetsId", variables);
//
//        Assertions.assertNotNull(TargetSetDtos);
//    }
//
//    @Test
//    void TargetSetController_AddTargetSet_Unsuccessful() {
//        variables.put("inputNewTargetSet", objectMapper.convertValue(
//                        createTestInputNewTargetSet(1L),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.empty());
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($inputNewTargetSet: InputNewTargetSet!){
//                       addTargetSet(inputNewTargetSet: $inputNewTargetSet) {
//                           id
//                           setIndex
//                           repetitionNumber
//                           weight
//                           weightUnit
//                           logDate
//                           targetSet {
//                               id
//                           }
//                       }
//                   }
//                """;
//
//        Assertions.assertThrows(QueryException.class,
//                () -> dgsQueryExecutor.executeAndExtractJsonPath(query, "data.addTargetSet", variables));
//    }
//
//    @Test
//    void TargetSetController_AddTargetSet_Success() {
//        variables.put("inputNewTargetSet", objectMapper.convertValue(
//                        createTestInputNewTargetSet(1L),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
//        when(targetSetService.save(Mockito.any(TargetSetEntity.class))).thenReturn(performanceLog);
//        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(performanceLogDto);
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($inputNewTargetSet: InputNewTargetSet!){
//                       addTargetSet(inputNewTargetSet: $inputNewTargetSet) {
//                           id
//                           setIndex
//                           repetitionNumber
//                           weight
//                           weightUnit
//                           logDate
//                           targetSet {
//                               id
//                           }
//                       }
//                   }
//                """;
//
//        LinkedHashMap<String, Object> TargetSetDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.addTargetSet", variables);
//
//        Assertions.assertNotNull(TargetSetDto);
//    }
//
//    @Test
//    void TargetSetController_ModifyTargetSet_Unsuccessful() {
//        variables.put("inputTargetSet", objectMapper.convertValue(
//                        createTestInputTargetSet(1L, 1L),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        when(targetSetService.exists(Mockito.any(Long.class))).thenReturn(false);
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($inputTargetSet: InputTargetSet!){
//                     modifyTargetSet(inputTargetSet: $inputTargetSet) {
//                         id
//                         setIndex
//                         repetitionNumber
//                         weight
//                         weightUnit
//                         logDate
//                         targetSet {
//                             id
//                         }
//                     }
//                 }
//                """;
//
//        LinkedHashMap<String, Object> TargetSetDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.modifyTargetSet", variables);
//
//        Assertions.assertNull(TargetSetDto);
//    }
//
//    @Test
//    void TargetSetController_ModifyTargetSet_Success() {
//        variables.put("inputTargetSet", objectMapper.convertValue(
//                        createTestInputTargetSet(1L, 1L),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//        when(targetSetService.exists(Mockito.any(Long.class))).thenReturn(true);
//        when(progExerciseService.findOne(Mockito.any(Long.class))).thenReturn(Optional.of(targetSet));
//        when(targetSetService.save(Mockito.any(TargetSetEntity.class))).thenReturn(performanceLog);
//        when(TargetSetMapper.mapTo(Mockito.any(TargetSetEntity.class))).thenReturn(performanceLogDto);
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($inputTargetSet: InputTargetSet!){
//                     modifyTargetSet(inputTargetSet: $inputTargetSet) {
//                         id
//                         setIndex
//                         repetitionNumber
//                         weight
//                         weightUnit
//                         logDate
//                         targetSet {
//                             id
//                         }
//                     }
//                 }
//                """;
//
//        LinkedHashMap<String, Object> TargetSetDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.modifyTargetSet", variables);
//
//        Assertions.assertNotNull(TargetSetDto);
//    }
//
//    @Test
//    void TargetSetController_DeleteTargetSet_Unsuccessful() {
//        variables.put("performanceLogId", 1);
//        when(targetSetService.exists(Mockito.any(Long.class))).thenReturn(false);
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($performanceLogId : Int!){
//                     deleteTargetSet(performanceLogId: $performanceLogId)
//                 }
//                """;
//
//        Integer id =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.deleteTargetSet", variables);
//
//        Assertions.assertNull(id);
//    }
//
//    @Test
//    void TargetSetController_DeleteTargetSet_Success() {
//        variables.put("performanceLogId", 1);
//        when(targetSetService.exists(Mockito.any(Long.class))).thenReturn(true);
//
//        @Language("GraphQL")
//        String query = """
//                 mutation ($performanceLogId : Int!){
//                     deleteTargetSet(performanceLogId: $performanceLogId)
//                 }
//                """;
//
//        Integer id =
//                dgsQueryExecutor.executeAndExtractJsonPath(query, "data.deleteTargetSet", variables);
//
//        Assertions.assertNotNull(id);
//    }
}