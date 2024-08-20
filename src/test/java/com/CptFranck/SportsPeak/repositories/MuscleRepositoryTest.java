package com.CptFranck.SportsPeak.repositories;

import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.CptFranck.SportsPeak.domain.TestDataUtil.createNewTestMuscle;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MuscleRepositoryTest {

    @Autowired
    private MuscleRepository muscleRepository;

    @Test
    public void MuscleRepository_Save_ReturnSavedMuscle() {
        MuscleEntity muscle = createNewTestMuscle();

        MuscleEntity savedMuscle = muscleRepository.save(muscle);

        Assertions.assertNotNull(savedMuscle);
        Assertions.assertTrue(muscle.getId() > 0L, "Id > 0");
    }

    @Test
    public void MuscleRepository_findAll_ReturnAllMuscle() {
        MuscleEntity muscleOne = createNewTestMuscle();
        MuscleEntity muscleTwo = createNewTestMuscle();
        muscleRepository.save(muscleOne);
        muscleRepository.save(muscleTwo);

        List<MuscleEntity> muscleEntities = StreamSupport.stream(
                        muscleRepository.findAll().spliterator(),
                        false)
                .toList();

        Assertions.assertNotNull(muscleEntities);
        Assertions.assertEquals(muscleEntities.size(), 2);
    }

    @Test
    public void MuscleRepository_findById_ReturnMuscle() {
        MuscleEntity muscle = createNewTestMuscle();
        MuscleEntity savedMuscle = muscleRepository.save(muscle);

        Optional<MuscleEntity> foundMuscle = muscleRepository.findById(savedMuscle.getId());

        Assertions.assertNotNull(foundMuscle);
        Assertions.assertTrue(foundMuscle.isPresent());
        Assertions.assertNotNull(foundMuscle.get());
    }
}
