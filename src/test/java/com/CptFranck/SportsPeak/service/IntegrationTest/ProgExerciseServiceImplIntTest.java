package com.CptFranck.SportsPeak.service.IntegrationTest;

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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.domain.utils.TestExerciseUtils.createTestExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createNewTestProgExerciseList;
import static com.CptFranck.SportsPeak.domain.utils.TestProgExerciseUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.domain.utils.TestUserUtils.createTestUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
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


    @BeforeEach
    void setUp() {
        user = userRepository.save(createTestUser(null));
        exercise = exerciseRepository.save(createTestExercise(null));
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
        List<ProgExerciseEntity> progExerciseList = StreamSupport.stream(
                progExerciseRepository.saveAll(createNewTestProgExerciseList(user, exercise)).spliterator(),
                false).toList();

        List<ProgExerciseEntity> progExerciseFound = progExerciseService.findAll();

        assertEqualProgExerciseList(progExerciseList, progExerciseFound);
    }

    @Test
    void progExerciseService_FindOne_Success() {
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));

        Optional<ProgExerciseEntity> progExerciseFound = progExerciseService.findOne(progExercise.getId());

        Assertions.assertTrue(progExerciseFound.isPresent());
        assertEqualProgExercise(progExercise, progExerciseFound.get());
    }

    @Test
    void progExerciseService_FindMany_Success() {
        List<ProgExerciseEntity> progExerciseList = StreamSupport.stream(
                progExerciseRepository.saveAll(createNewTestProgExerciseList(user, exercise)).spliterator(),
                false).toList();
        Set<Long> progExerciseIds = progExerciseList.stream().map(ProgExerciseEntity::getId).collect(Collectors.toSet());

        Set<ProgExerciseEntity> progExerciseFound = progExerciseService.findMany(progExerciseIds);

        assertEqualProgExerciseList(progExerciseList, progExerciseFound.stream().toList());
    }

    @Test
    void progExerciseService_Exists_Success() {
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));

        boolean progExerciseFound = progExerciseService.exists(progExercise.getId());

        Assertions.assertTrue(progExerciseFound);
    }

    @Test
    void progExerciseService_Delete_Success() {
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));

        assertAll(() -> progExerciseService.delete(progExercise.getId()));
    }

    @Test
    void progExerciseService_Delete_Unsuccessful() {
        ProgExerciseEntity progExercise = progExerciseRepository.save(createTestProgExercise(null, user, exercise));
        progExerciseRepository.delete(progExercise);

        assertThrows(ProgExerciseNotFoundException.class, () -> progExerciseService.delete(progExercise.getId()));
    }

    private void assertEqualProgExerciseList(
            List<ProgExerciseEntity> expectedProgExerciseList,
            List<ProgExerciseEntity> progExerciseListObtained
    ) {
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
