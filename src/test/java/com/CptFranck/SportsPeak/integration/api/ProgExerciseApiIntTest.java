package com.CptFranck.SportsPeak.integration.api;

import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputNewProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExerciseTrustLabel;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.repositories.ProgExerciseRepository;
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

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static com.CptFranck.SportsPeak.integration.api.graphqlQueries.ProgExerciseQuery.*;
import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.*;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUserBis;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class ProgExerciseApiIntTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DgsQueryExecutor dgsQueryExecutor;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    private UserEntity user;
    private ExerciseEntity exercise;
    private ProgExerciseEntity progExercise;
    private LinkedHashMap<String, Object> variables;

    @BeforeEach
    void init() {
        user = userRepository.save(createTestUser(null));
        exercise = exerciseRepository.save(createTestExercise(null));
        progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        variables = new LinkedHashMap<>();
    }

    @AfterEach
    public void afterEach() {
        userRepository.findAll().forEach(user -> {
            user.setSubscribedProgExercises(new HashSet<>());
            userRepository.save(user);
        });
        this.progExerciseRepository.deleteAll();
        this.exerciseRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void ProgExerciseApi_GetProgExercises_Success() {
        List<LinkedHashMap<String, Object>> response = dgsQueryExecutor.executeAndExtractJsonPath(getProgExercisesQuery,
                "data.getProgExercises");

        List<ProgExerciseDto> progExerciseDtos = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertEqualProgExerciseList(List.of(progExercise), progExerciseDtos);
    }

    @Test
    void ProgExerciseApi_GetProgExerciseById_UnsuccessfulProgExerciseNotFound() {
        variables.put("id", progExercise.getId() + 1);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(getProgExerciseByIdQuery, "data.getProgExerciseById", variables));

        Assertions.assertTrue(exception.getMessage().contains("ProgExerciseNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The progExercise with the id %s has not been found", progExercise.getId() + 1)));
    }

    @Test
    void ProgExerciseApi_GetProgExerciseById_Success() {
        variables.put("id", progExercise.getId());

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(getProgExerciseByIdQuery,
                "data.getProgExerciseById", variables);

        ProgExerciseDto progExerciseDto = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertProgExerciseDtoAndEntity(progExercise, progExerciseDto);
    }

    @Test
    void ProgExerciseApi_AddProgExercise_UnsuccessfulNotAuthenticated() {
        variables.put("inputNewProgExercise", objectMapper.convertValue(
                createTestInputNewProgExercise(user.getId(), exercise.getId()),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPath(addProgExerciseQuery,
                "data.addProgExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseApi_AddProgExercise_UnsuccessfulUserNotFound() {
        variables.put("inputNewProgExercise", objectMapper.convertValue(
                createTestInputNewProgExercise(user.getId() + 1, exercise.getId()),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPath(addProgExerciseQuery,
                "data.addProgExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("UserNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("User with id %s has been not found", user.getId() + 1)));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseApi_AddProgExercise_UnsuccessfulExerciseNotFound() {
        variables.put("inputNewProgExercise", objectMapper.convertValue(
                createTestInputNewProgExercise(user.getId(), exercise.getId() + 1),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPath(addProgExerciseQuery,
                "data.addProgExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("ExerciseNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The exercise with the id %s has not been found", exercise.getId() + 1)));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseApi_AddProgExercise_Success() {
        InputNewProgExercise newProgExercise = createTestInputNewProgExercise(user.getId(), exercise.getId());
        variables.put("inputNewProgExercise", objectMapper.convertValue(
                newProgExercise,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(addProgExerciseQuery,
                "data.addProgExercise", variables);

        ProgExerciseDto progExerciseDto = objectMapper.convertValue(response, new TypeReference<>() {
        });
        assertProgExerciseDtoAndInputNew(newProgExercise, progExerciseDto);
    }

    @Test
    void ProgExerciseApi_ModifyProgExercise_UnsuccessfulNotAuthenticated() {
        variables.put("inputProgExercise", objectMapper.convertValue(
                createTestInputProgExercise(progExercise.getId(), exercise.getId(), false),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseQuery,
                "data.modifyProgExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseApi_ModifyProgExercise_UnsuccessfulProgExerciseNotFound() {
        variables.put("inputProgExercise", objectMapper.convertValue(
                createTestInputProgExercise(progExercise.getId() + 1, exercise.getId(), false),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseQuery, "data.modifyProgExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("ProgExerciseNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The progExercise with the id %s has not been found", progExercise.getId() + 1)));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseApi_ModifyProgExercise_UnsuccessfulExerciseNotFound() {
        variables.put("inputProgExercise", objectMapper.convertValue(
                createTestInputProgExercise(progExercise.getId(), exercise.getId() + 1, false),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseQuery, "data.modifyProgExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("ExerciseNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The exercise with the id %s has not been found", exercise.getId() + 1)));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseApi_ModifyProgExercise_UnsuccessfulWrongLabel() {
        InputProgExercise inputProgExercise = createTestInputProgExercise(progExercise.getId(), exercise.getId(), true);
        variables.put("inputProgExercise", objectMapper.convertValue(inputProgExercise, new TypeReference<LinkedHashMap<String, Object>>() {
        }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseQuery, "data.modifyProgExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("LabelMatchNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("No %s (label) match with %s", "VisibilityLabel", inputProgExercise.getVisibility())));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseApi_ModifyProgExercise_Success() {
        InputProgExercise inputNewExercise = createTestInputProgExercise(progExercise.getId(), exercise.getId(), false);
        variables.put("inputProgExercise", objectMapper.convertValue(inputNewExercise, new TypeReference<LinkedHashMap<String, Object>>() {
        }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseQuery,
                "data.modifyProgExercise", variables);

        ProgExerciseDto progExerciseDto = objectMapper.convertValue(response, ProgExerciseDto.class);
        assertProgExerciseDtoAndInput(inputNewExercise, progExerciseDto);
    }

    @Test
    void ProgExerciseApi_ModifyProgExerciseTrustLabel_UnsuccessfulNotAuthenticated() {
        variables.put("inputProgExerciseTrustLabel", objectMapper.convertValue(
                createTestInputProgExerciseTrustLabel(progExercise.getId(), false),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class, () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseTrustLabelQuery,
                "data.modifyProgExerciseTrustLabel", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ProgExerciseApi_ModifyProgExerciseTrustLabel_UnsuccessfulProgExerciseNotFound() {
        variables.put("inputProgExerciseTrustLabel", objectMapper.convertValue(
                createTestInputProgExerciseTrustLabel(progExercise.getId() + 1, false),
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseTrustLabelQuery, "data.modifyProgExerciseTrustLabel", variables));

        Assertions.assertTrue(exception.getMessage().contains("ProgExerciseNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The progExercise with the id %s has not been found", progExercise.getId() + 1)));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ProgExerciseApi_ModifyProgExerciseTrustLabel_UnsuccessfulWrongLabel() {
        InputProgExerciseTrustLabel inputProgExerciseTrustLabel = createTestInputProgExerciseTrustLabel(progExercise.getId(), true);
        variables.put("inputProgExerciseTrustLabel", objectMapper.convertValue(
                inputProgExerciseTrustLabel,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseTrustLabelQuery, "data.modifyProgExerciseTrustLabel", variables));

        Assertions.assertTrue(exception.getMessage().contains("LabelMatchNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("No %s (label) match with %s", "VisibilityLabel", inputProgExerciseTrustLabel.getTrustLabel())));
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void ProgExerciseApi_ModifyProgExerciseTrustLabel_Success() {
        InputProgExerciseTrustLabel inputProgExerciseTrustLabel = createTestInputProgExerciseTrustLabel(progExercise.getId(), false);
        variables.put("inputProgExerciseTrustLabel", objectMapper.convertValue(
                inputProgExerciseTrustLabel,
                new TypeReference<LinkedHashMap<String, Object>>() {
                }));

        LinkedHashMap<String, Object> response = dgsQueryExecutor.executeAndExtractJsonPath(modifyProgExerciseTrustLabelQuery,
                "data.modifyProgExerciseTrustLabel", variables);

        ProgExerciseDto progExerciseDto = objectMapper.convertValue(response, ProgExerciseDto.class);
        Assertions.assertEquals(inputProgExerciseTrustLabel.getId(), progExerciseDto.getId());
        Assertions.assertEquals(inputProgExerciseTrustLabel.getTrustLabel(), progExerciseDto.getTrustLabel());
    }

    @Test
    void ProgExerciseApi_DeleteProgExercise_UnsuccessfulNotAuthenticated() {
        variables.put("progExerciseId", progExercise.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteProgExerciseQuery, "data.deleteProgExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("AuthenticationCredentialsNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains("An Authentication object was not found in the SecurityContext"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseApi_DeleteProgExercise_UnsuccessfulProgExerciseNotFound() {
        variables.put("progExerciseId", progExercise.getId() + 1);

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteProgExerciseQuery, "data.deleteProgExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("ProgExerciseNotFoundException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The progExercise with the id %s has not been found", progExercise.getId() + 1)));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseApi_DeleteProgExercise_UnsuccessfulProgExerciseStillUsed() {
        UserEntity userBis = createTestUserBis(null);
        userBis.getSubscribedProgExercises().add(progExercise);
        userRepository.save(userBis);

        variables.put("progExerciseId", progExercise.getId());

        QueryException exception = Assertions.assertThrows(QueryException.class,
                () -> dgsQueryExecutor.executeAndExtractJsonPath(deleteProgExerciseQuery, "data.deleteProgExercise", variables));

        Assertions.assertTrue(exception.getMessage().contains("ProgExerciseStillUsedException"));
        Assertions.assertTrue(exception.getMessage().contains(String.format("The progExercise with the id %s still used by some user", progExercise.getId())));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void ProgExerciseApi_DeleteProgExercise_Success() {
        variables.put("progExerciseId", progExercise.getId());

        Integer id = dgsQueryExecutor.executeAndExtractJsonPath(deleteProgExerciseQuery, "data.deleteProgExercise", variables);

        Assertions.assertEquals(progExercise.getId().intValue(), id);
    }

    private void assertEqualProgExerciseList(
            List<ProgExerciseEntity> progExerciseEntities,
            List<ProgExerciseDto> progExerciseDtos
    ) {
        Assertions.assertEquals(progExerciseEntities.size(), progExerciseDtos.size());
        progExerciseDtos.forEach(progExerciseDto -> assertProgExerciseDtoAndEntity(
                progExerciseEntities.stream().filter(
                        progExercise -> Objects.equals(progExercise.getId(), progExerciseDto.getId())
                ).toList().getFirst(),
                progExerciseDto)
        );
    }

    private void assertProgExerciseDtoAndEntity(ProgExerciseEntity progExercise, ProgExerciseDto progExerciseDto) {
        Assertions.assertNotNull(progExerciseDto);
        Assertions.assertEquals(progExercise.getName(), progExerciseDto.getName());
        Assertions.assertEquals(progExercise.getNote(), progExerciseDto.getNote());
        Assertions.assertEquals(progExercise.getVisibility().label, progExerciseDto.getVisibility());
        Assertions.assertEquals(progExercise.getTrustLabel().label, progExerciseDto.getTrustLabel());
        Assertions.assertEquals(progExercise.getCreator().getId(), progExerciseDto.getCreator().getId());
        Assertions.assertEquals(progExercise.getExercise().getId(), progExerciseDto.getExercise().getId());
        Assertions.assertEquals(progExercise.getTargetSets().size(), progExerciseDto.getTargetSets().size());
    }

    private void assertProgExerciseDtoAndInputNew(InputNewProgExercise inputNewProgExercise, ProgExerciseDto progExerciseDto) {
        Assertions.assertNotNull(progExerciseDto);
        Assertions.assertEquals(inputNewProgExercise.getName(), progExerciseDto.getName());
        Assertions.assertEquals(inputNewProgExercise.getNote(), progExerciseDto.getNote());
        Assertions.assertEquals(inputNewProgExercise.getVisibility(), progExerciseDto.getVisibility());
        Assertions.assertEquals(inputNewProgExercise.getCreatorId(), progExerciseDto.getCreator().getId());
        Assertions.assertEquals(inputNewProgExercise.getExerciseId(), progExerciseDto.getExercise().getId());
    }

    private void assertProgExerciseDtoAndInput(InputProgExercise inputProgExercise, ProgExerciseDto progExerciseDto) {
        Assertions.assertNotNull(progExerciseDto);
        Assertions.assertEquals(inputProgExercise.getName(), progExerciseDto.getName());
        Assertions.assertEquals(inputProgExercise.getNote(), progExerciseDto.getNote());
        Assertions.assertEquals(inputProgExercise.getVisibility(), progExerciseDto.getVisibility());
        Assertions.assertEquals(inputProgExercise.getExerciseId(), progExerciseDto.getExercise().getId());
    }
}