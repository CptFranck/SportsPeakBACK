package com.CptFranck.SportsPeak.integration.api;

import com.CptFranck.SportsPeak.domain.dto.TargetSetDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputNewTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSet;
import com.CptFranck.SportsPeak.domain.input.targetSet.InputTargetSetState;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.repositories.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repositories.TargetSetRepository;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.exceptions.QueryException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.integration.api.graphqlQueries.TargetSetQuery.*;
import static com.CptFranck.SportsPeak.utils.TestDateTimeUtils.assertDatetimeWithTimestamp;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestInputDuration.assertInputDuration;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestTargetSetUtils.*;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class TargetSetApiIT {

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Autowired
    private TargetSetRepository targetSetRepository;

    private ProgExerciseEntity progExercise;
    private TargetSetEntity targetSet;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void setUp() {
        UserEntity user = userRepository.save(createTestUser(null));
        ExerciseEntity exercise = exerciseRepository.save(createTestExercise(null));
        progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        targetSet = targetSetRepository.save(createTestTargetSet(null, progExercise, null));
        variables = new LinkedHashMap<>();
    }

    @AfterEach
    public void afterEach() {
        this.targetSetRepository.deleteAll();
        this.progExerciseRepository.deleteAll();
        this.exerciseRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void getTargetSets_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getTargetSetsQuery, "data.getTargetSets"));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getTargetSets_ValidUse_ReturnListOfTargetSetDto() {
        List<LinkedHashMap<String, Object>> response = dgsQueryExecutor.executeAndExtractJsonPath(getTargetSetsQuery,
                "data.getTargetSets");

        List<TargetSetDto> targetSetDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertEqualTargeSetList(List.of(targetSet), targetSetDtos);
    }

    @Test
    void getTargetSetById_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        variables.put("id", targetSet.getId());
        targetSetRepository.delete(targetSet);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getTargetSetByIdQuery, "data.getTargetSetById", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getTargetSetById_InvalidTargetSetId_ThrowTargetSetNotFoundException() {
        variables.put("id", targetSet.getId());
        targetSetRepository.delete(targetSet);

        QueryException exception = Assertions.assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPath(getTargetSetByIdQuery,
                "data.getTargetSetById", variables));

        Assertions.assertTrue(exception.getMessage().contains("TargetSetNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The targetSet with the id %s has not been found", targetSet.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getTargetSetById_ValidInput_ReturnTargetSetDto() {
        variables.put("id", targetSet.getId());

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(getTargetSetByIdQuery,
                "data.getTargetSetById", variables);

        TargetSetDto targetSetDto = objectMapper.convertValue(response, TargetSetDto.class);
        assertTargetSetDtoAndEntity(targetSet, targetSetDto);
    }

    @Test
    void getTargetSetsByProgExerciseId_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        variables.put("progExerciseId", progExercise.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getTargetSetsByProgExerciseIdQuery, "data.getTargetSetsByProgExerciseId", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getTargetSetsByProgExerciseId_ValidInput_ReturnListOfTargetSetDto() {
        variables.put("progExerciseId", progExercise.getId());

        List<LinkedHashMap<String, Object>> response = dgsQueryExecutor.executeAndExtractJsonPath(getTargetSetsByProgExerciseIdQuery,
                "data.getTargetSetsByProgExerciseId", variables);

        List<TargetSetDto> targetSetDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertEqualTargeSetList(List.of(targetSet), targetSetDtos);
    }

    @Test
    void addTargetSet_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        variables.put("inputNewTargetSet", objectMapper.convertValue(
                createTestInputNewTargetSet(progExercise.getId(), null, false),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addTargetSetQuery, "data.addTargetSet", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void addTargetSet_InvalidProgExerciseId_ThrowProgExerciseNotFoundException() {
        variables.put("inputNewTargetSet", objectMapper.convertValue(
                createTestInputNewTargetSet(progExercise.getId(), null, false),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));
        targetSetRepository.delete(targetSet);
        progExerciseRepository.delete(progExercise);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addTargetSetQuery, "data.addTargetSet", variables));

        Assertions.assertTrue(exception.getMessage().contains("ProgExerciseNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The progExercise with the id %s has not been found", progExercise.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void addTargetSet_InvalidInputLabel_ThrowLabelMatchNotFoundException() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), null, true);
        variables.put("inputNewTargetSet", objectMapper.convertValue(
                inputNewTargetSet,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addTargetSetQuery, "data.addTargetSet", variables));

        Assertions.assertTrue(exception.getMessage().contains("LabelMatchNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("No %s (label) match with %s", "WeightUnit", inputNewTargetSet.getWeightUnit())));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void addTargetSet_ValidInput_ReturnTargetSetDto() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), null, false);
        variables.put("inputNewTargetSet", objectMapper.convertValue(
                inputNewTargetSet,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(addTargetSetQuery,
                "data.addTargetSet", variables);

        TargetSetDto targetSetDto = objectMapper.convertValue(response, TargetSetDto.class);
        assertTargetSetDtoAndInputNew(inputNewTargetSet, targetSetDto);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void addTargetSet_ValidInputWithUpdate_ThrowProgExerciseNotFoundException() {
        variables.put("inputNewTargetSet", objectMapper.convertValue(
                createTestInputNewTargetSet(progExercise.getId(), targetSet.getId(), false),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));
        targetSetRepository.delete(targetSet);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(addTargetSetQuery, "data.addTargetSet", variables));

        Assertions.assertTrue(exception.getMessage().contains("TargetSetNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The targetSet with the id %s has not been found", targetSet.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void addTargetSet_ValidInputWithUpdate_ReturnTargetSetDto() {
        InputNewTargetSet inputNewTargetSet = createTestInputNewTargetSet(progExercise.getId(), targetSet.getId(), false);
        variables.put("inputNewTargetSet", objectMapper.convertValue(
                inputNewTargetSet,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(addTargetSetQuery,
                "data.addTargetSet", variables);

        TargetSetDto targetSetDto = objectMapper.convertValue(response, TargetSetDto.class);
        assertTargetSetDtoAndInputNew(inputNewTargetSet, targetSetDto);
    }

    @Test
    void modifyTargetSet_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        variables.put("inputTargetSet", objectMapper.convertValue(
                createTestInputTargetSet(targetSet.getId(), false),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyTargetSetQuery, "data.modifyTargetSet", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyTargetSet_InvalidTargetSetId_ThrowTargetSetNotFoundException() {
        variables.put("inputTargetSet", objectMapper.convertValue(
                createTestInputTargetSet(targetSet.getId(), false),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));
        targetSetRepository.delete(targetSet);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyTargetSetQuery, "data.modifyTargetSet", variables));

        Assertions.assertTrue(exception.getMessage().contains("TargetSetNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The targetSet with the id %s has not been found", targetSet.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyTargetSet_InvalidInputLabel_ThrowLabelMatchNotFoundException() {
        InputTargetSet inputTargetSet = createTestInputTargetSet(targetSet.getId(), true);
        variables.put("inputTargetSet", objectMapper.convertValue(
                inputTargetSet,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyTargetSetQuery, "data.modifyTargetSet", variables));

        Assertions.assertTrue(exception.getMessage().contains("LabelMatchNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("No %s (label) match with %s", "WeightUnit", inputTargetSet.getWeightUnit())));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyTargetSet_ValidInput_ReturnTargetSetDto() {
        InputTargetSet inputTargetSet = createTestInputTargetSet(targetSet.getId(), false);
        variables.put("inputTargetSet", objectMapper.convertValue(
                inputTargetSet,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(modifyTargetSetQuery,
                "data.modifyTargetSet", variables);

        TargetSetDto targetSetDto = objectMapper.convertValue(response, TargetSetDto.class);
        assertTargetSetDtoAndInput(inputTargetSet, targetSetDto);
    }

    @Test
    void modifyTargetSetState_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        variables.put("inputTargetSetState", objectMapper.convertValue(
                createTestInputInputTargetSetState(targetSet.getId(), false),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyTargetSetStateQuery, "data.modifyTargetSetState", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyTargetSetState_InvalidTargetSetId_ThrowTargetSetNotFoundException() {
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(targetSet.getId(), false);
        variables.put("inputTargetSetState", objectMapper.convertValue(
                inputTargetSetState,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));
        targetSetRepository.delete(targetSet);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyTargetSetStateQuery, "data.modifyTargetSetState", variables));

        Assertions.assertTrue(exception.getMessage().contains("TargetSetNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The targetSet with the id %s has not been found", targetSet.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyTargetSetState_InvalidInputLabel_ThrowLabelMatchNotFoundException() {
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(targetSet.getId(), true);
        variables.put("inputTargetSetState", objectMapper.convertValue(
                inputTargetSetState,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyTargetSetStateQuery, "data.modifyTargetSetState", variables));

        Assertions.assertTrue(exception.getMessage().contains("LabelMatchNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("No %s (label) match with %s", "VisibilityLabel", inputTargetSetState.getState())));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void modifyTargetSetState_validInput_ReturnListOfTargetSetDto() {
        targetSetRepository.save(createTestTargetSet(null, progExercise, targetSet));
        InputTargetSetState inputTargetSetState = createTestInputInputTargetSetState(targetSet.getId(), false);
        variables.put("inputTargetSetState", objectMapper.convertValue(
                inputTargetSetState,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        List<LinkedHashMap<String, Object>> response = dgsQueryExecutor.executeAndExtractJsonPath(modifyTargetSetStateQuery,
                "data.modifyTargetSetState", variables);

        List<TargetSetDto> targetSetDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        targetSetDtos.forEach(targetSetDto -> Assertions.assertEquals(targetSetDto.getState(), inputTargetSetState.getState()));
    }

    @Test
    void deleteTargetSet_NotAuthenticated_ThrowQueryAuthenticationCredentialsNotFoundException() {
        variables.put("targetSetId", targetSet.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteTargetSetQuery, "data.deleteTargetSet", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void deleteTargetSet_InvalidTargetSetId_ThrowTargetSetNotFoundException() {
        variables.put("targetSetId", targetSet.getId());
        targetSetRepository.delete(targetSet);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteTargetSetQuery, "data.deleteTargetSet", variables));

        Assertions.assertTrue(exception.getMessage().contains("TargetSetNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The targetSet with the id %s has not been found", targetSet.getId())));

    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void deleteTargetSet_ValidInput_ReturnExerciseId() {
        variables.put("targetSetId", targetSet.getId());

        String id = dgsQueryExecutor.executeAndExtractJsonPath(deleteTargetSetQuery, "data.deleteTargetSet", variables);

        Assertions.assertEquals(targetSet.getId(), Long.valueOf(id));
    }

    private void assertEqualTargeSetList(
            List<TargetSetEntity> targetSetEntities,
            List<TargetSetDto> targetSetDtos
    ) {
        Assertions.assertEquals(targetSetEntities.size(), targetSetDtos.size());
        targetSetDtos.forEach(targetSetDto -> assertTargetSetDtoAndEntity(
                targetSetEntities.stream().filter(
                        targetSetEntity -> Objects.equals(targetSetEntity.getId(), targetSetDto.getId())
                ).toList().getFirst(),
                targetSetDto)
        );
    }

    private void assertTargetSetDtoAndEntity(TargetSetEntity targetSetEntity, TargetSetDto targetSetDto) {
        Assertions.assertNotNull(targetSetDto);
        Assertions.assertEquals(targetSetEntity.getId(), targetSetDto.getId());
        Assertions.assertEquals(targetSetEntity.getIndex(), targetSetDto.getIndex());
        Assertions.assertEquals(targetSetEntity.getSetNumber(), targetSetDto.getSetNumber());
        Assertions.assertEquals(targetSetEntity.getRepetitionNumber(), targetSetDto.getRepetitionNumber());
        Assertions.assertEquals(targetSetEntity.getWeight(), targetSetDto.getWeight());
        Assertions.assertEquals(targetSetEntity.getWeightUnit().label, targetSetDto.getWeightUnit());
        Assertions.assertEquals(targetSetEntity.getPhysicalExertionUnitTime(), targetSetDto.getPhysicalExertionUnitTime().InputDurationToDuration());
        Assertions.assertEquals(targetSetEntity.getRestTime(), targetSetDto.getRestTime().InputDurationToDuration());
        assertDatetimeWithTimestamp(targetSetEntity.getCreationDate(), targetSetDto.getCreationDate());
        Assertions.assertEquals(targetSetEntity.getState().label, targetSetDto.getState());
        if (Objects.nonNull(targetSetEntity.getTargetSetUpdate()) || Objects.nonNull(targetSetDto.getTargetSetUpdate()))
            Assertions.assertEquals(targetSetEntity.getTargetSetUpdate().getId(), targetSetDto.getTargetSetUpdate().getId());
        Assertions.assertEquals(targetSetEntity.getPerformanceLogs().size(), targetSetDto.getPerformanceLogs().size());
    }

    private void assertTargetSetDtoAndInputNew(InputNewTargetSet inputNewTargetSet, TargetSetDto targetSetDto) {
        Assertions.assertNotNull(targetSetDto);
        Assertions.assertEquals(inputNewTargetSet.getIndex(), targetSetDto.getIndex());
        Assertions.assertEquals(inputNewTargetSet.getSetNumber(), targetSetDto.getSetNumber());
        Assertions.assertEquals(inputNewTargetSet.getRepetitionNumber(), targetSetDto.getRepetitionNumber());
        Assertions.assertEquals(inputNewTargetSet.getWeight(), targetSetDto.getWeight());
        Assertions.assertEquals(inputNewTargetSet.getWeightUnit(), targetSetDto.getWeightUnit());
        assertInputDuration(inputNewTargetSet.getPhysicalExertionUnitTime(), targetSetDto.getPhysicalExertionUnitTime());
        assertInputDuration(inputNewTargetSet.getRestTime(), targetSetDto.getRestTime());
        assertDatetimeWithTimestamp(inputNewTargetSet.getCreationDate(), targetSetDto.getCreationDate());
    }

    private void assertTargetSetDtoAndInput(InputTargetSet targetSetEntity, TargetSetDto targetSetDto) {
        Assertions.assertNotNull(targetSetDto);
        Assertions.assertEquals(targetSetEntity.getId(), targetSetDto.getId());
        Assertions.assertEquals(targetSetEntity.getIndex(), targetSetDto.getIndex());
        Assertions.assertEquals(targetSetEntity.getSetNumber(), targetSetDto.getSetNumber());
        Assertions.assertEquals(targetSetEntity.getRepetitionNumber(), targetSetDto.getRepetitionNumber());
        Assertions.assertEquals(targetSetEntity.getWeight(), targetSetDto.getWeight());
        Assertions.assertEquals(targetSetEntity.getWeightUnit(), targetSetDto.getWeightUnit());
        assertInputDuration(targetSetEntity.getPhysicalExertionUnitTime(), targetSetDto.getPhysicalExertionUnitTime());
        assertInputDuration(targetSetEntity.getRestTime(), targetSetDto.getRestTime());
    }
}