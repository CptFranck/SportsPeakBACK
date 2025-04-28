package com.CptFranck.SportsPeak.integration.services;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseNotFoundException;
import com.CptFranck.SportsPeak.repositories.ExerciseRepository;
import com.CptFranck.SportsPeak.repositories.ProgExerciseRepository;
import com.CptFranck.SportsPeak.repositories.UserRepository;
import com.CptFranck.SportsPeak.service.impl.ProgExerciseServiceImpl;
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

import static com.CptFranck.SportsPeak.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.TestUserUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class ProgExerciseServiceImplIntTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ProgExerciseRepository progExerciseRepository;

    @Autowired
    private ProgExerciseServiceImpl progExerciseService;

    private UserEntity user;
    private ExerciseEntity exercise;
    private ProgExerciseEntity progExercise;


    @BeforeEach
    void setUp() {
        user = userRepository.save(createTestUser(null));
        exercise = exerciseRepository.save(createTestExercise(null));
        progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
    }

    @AfterEach
    public void afterEach() {
        this.progExerciseRepository.deleteAll();
        this.exerciseRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void progExerciseService_Save_Success() {
        ProgExerciseEntity unsavedProgExercise = createTestProgExercise(null, user, exercise);

        ProgExerciseEntity progExerciseSaved = progExerciseService.save(unsavedProgExercise);

        assertEqualProgExercise(unsavedProgExercise, progExerciseSaved);
    }

    @Test
    void progExerciseService_FindAll_Success() {
        List<ProgExerciseEntity> progExerciseFound = progExerciseService.findAll();

        assertEqualProgExerciseList(List.of(progExercise), progExerciseFound);
    }

    @Test
    void progExerciseService_FindOne_Success() {
        Optional<ProgExerciseEntity> progExerciseFound = progExerciseService.findOne(progExercise.getId());

        Assertions.assertTrue(progExerciseFound.isPresent());
        assertEqualProgExercise(progExercise, progExerciseFound.get());
    }

    @Test
    void progExerciseService_FindMany_Success() {
        Set<ProgExerciseEntity> progExerciseFound = progExerciseService.findMany(Set.of(progExercise.getId()));

        assertEqualProgExerciseList(List.of(progExercise), progExerciseFound.stream().toList());
    }

    @Test
    void progExerciseService_Exists_Success() {
        boolean progExerciseFound = progExerciseService.exists(progExercise.getId());

        Assertions.assertTrue(progExerciseFound);
    }

    @Test
    void progExerciseService_Delete_Success() {
        assertAll(() -> progExerciseService.delete(progExercise.getId()));
    }

    @Test
    void progExerciseService_Delete_Unsuccessful() {
        progExerciseRepository.delete(progExercise);

        assertThrows(ProgExerciseNotFoundException.class, () -> progExerciseService.delete(progExercise.getId()));
    }

    private void assertEqualProgExerciseList(
            List<ProgExerciseEntity> expectedProgExerciseList,
            List<ProgExerciseEntity> progExerciseListObtained
    ) {
        Assertions.assertEquals(expectedProgExerciseList.size(), progExerciseListObtained.size());
        expectedProgExerciseList.forEach(progExerciseFound -> assertEqualProgExercise(
                progExerciseListObtained.stream().filter(
                        progExercise -> Objects.equals(progExercise.getId(), progExerciseFound.getId())
                ).toList().getFirst(),
                progExerciseFound)
        );
    }

    private void assertEqualProgExercise(ProgExerciseEntity expected, ProgExerciseEntity actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getVisibility(), actual.getVisibility());
        Assertions.assertEquals(expected.getTrustLabel(), actual.getTrustLabel());
    }
}
