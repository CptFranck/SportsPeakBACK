package com.CptFranck.SportsPeak.domain.entity.relationEntity;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.entity.compositeKey.SolicitedMuscleKey;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "solicited_muscle")
public class SolicitedMuscleEntity {
    @EmbeddedId
    SolicitedMuscleKey id;

    @ManyToOne
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id")
    ExerciseEntity exercise;

    @ManyToOne
    @MapsId("muscleId")
    @JoinColumn(name = "muscle_id")
    MuscleEntity muscle;
}
