package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.TestDataUtil;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestMuscle;
import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestMuscles;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MuscleRepositoryTest {

    @Autowired
    private MuscleRepository muscleRepository;

    private MuscleEntity saveOneMuscleInRepository() {
        MuscleEntity muscle = createNewTestMuscle();
        return muscleRepository.save(muscle);
    }

    private List<MuscleEntity> saveAllMusclesInRepository(List<MuscleEntity> muscles) {
        List<MuscleEntity> localMuscle = Objects.requireNonNullElseGet(muscles, TestDataUtil::createNewTestMuscles);
        muscleRepository.saveAll(localMuscle);
        return localMuscle;
    }

    @Test
    public void MuscleRepository_Save_ReturnSavedMuscle() {
        MuscleEntity muscle = createNewTestMuscle();

        MuscleEntity savedMuscle = muscleRepository.save(muscle);

        Assertions.assertNotNull(savedMuscle);
        Assertions.assertTrue(muscle.getId() > 0L, "Id > 0");
    }

    @Test
    public void MuscleRepository_SaveAll_ReturnSavedMuscles() {
        List<MuscleEntity> muscles = createNewTestMuscles();

        List<MuscleEntity> savedMuscles = saveAllMusclesInRepository(muscles);

        Assertions.assertNotNull(savedMuscles);
        Assertions.assertEquals(muscles.size(), savedMuscles.size());
        Assertions.assertTrue(muscles.getFirst().getId() > 0L, "first user Id must be > 0");
        Assertions.assertTrue(muscles.getLast().getId() > 1L, "second user Id must be > 0");
    }

    @Test
    public void MuscleRepository_FindById_ReturnMuscle() {
        MuscleEntity muscle = createNewTestMuscle();
        MuscleEntity savedMuscle = muscleRepository.save(muscle);

        Optional<MuscleEntity> foundMuscle = muscleRepository.findById(savedMuscle.getId());

        Assertions.assertNotNull(foundMuscle);
        Assertions.assertTrue(foundMuscle.isPresent());
        Assertions.assertNotNull(foundMuscle.get());
    }

    @Test
    public void MuscleRepository_FindAll_ReturnAllMuscles() {
        saveAllMusclesInRepository(null);

        List<MuscleEntity> muscleEntities = StreamSupport.stream(
                        muscleRepository.findAll().spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(muscleEntities);
        Assertions.assertEquals(muscleEntities.size(), 2);
    }

    @Test
    public void MuscleRepository_FindAllById_ReturnAllMuscles() {
        List<MuscleEntity> muscles = createNewTestMuscles();
        muscleRepository.saveAll(muscles);

        List<MuscleEntity> muscleEntities = StreamSupport.stream(
                        muscleRepository.findAllById(muscles.stream()
                                        .map(MuscleEntity::getId)
                                        .toList())
                                .spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(muscleEntities);
        Assertions.assertEquals(2, muscleEntities.size());
    }

    @Test
    public void MuscleRepository_ExistById_ReturnTrue() {
        MuscleEntity muscle = createNewTestMuscle();
        MuscleEntity savedMuscle = muscleRepository.save(muscle);

        boolean foundMuscle = muscleRepository.existsById(savedMuscle.getId());

        Assertions.assertTrue(foundMuscle);
    }

    @Test
    public void MuscleRepository_Count_ReturnMusclesListSize() {
        List<MuscleEntity> savedMuscles = saveAllMusclesInRepository(null);

        Long userCount = muscleRepository.count();

        Assertions.assertEquals(userCount, savedMuscles.size());
    }

    @Test
    public void MuscleRepository_Delete_ReturnFalse() {
        MuscleEntity savedMuscle = saveOneMuscleInRepository();

        muscleRepository.delete(savedMuscle);
        boolean foundMuscle = muscleRepository.existsById(savedMuscle.getId());

        Assertions.assertFalse(foundMuscle);
    }

    @Test
    public void MuscleRepository_DeleteById_ReturnTrue() {
        MuscleEntity muscle = createNewTestMuscle();
        MuscleEntity savedMuscle = muscleRepository.save(muscle);

        muscleRepository.deleteById(savedMuscle.getId());
        boolean foundMuscle = muscleRepository.existsById(savedMuscle.getId());

        Assertions.assertFalse(foundMuscle);
    }

    @Test
    public void MuscleRepository_DeleteAllById_ReturnAllFalse() {
        List<MuscleEntity> muscleEntities = saveAllMusclesInRepository(null);

        muscleRepository.deleteAllById(muscleEntities.stream().map(MuscleEntity::getId).toList());

        muscleEntities.forEach(muscle -> {
            boolean foundMuscle = muscleRepository.existsById(muscle.getId());
            Assertions.assertFalse(foundMuscle);
        });
    }

    @Test
    public void MuscleRepository_DeleteAll_ReturnAllFalse() {
        List<MuscleEntity> muscleEntities = saveAllMusclesInRepository(null);

        muscleRepository.deleteAll();

        muscleEntities.forEach(muscle -> {
            boolean foundMuscle = muscleRepository.existsById(muscle.getId());
            Assertions.assertFalse(foundMuscle);
        });
    }
}
