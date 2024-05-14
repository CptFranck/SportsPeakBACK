package com.CptFranck.SportsPeak.domain.entity.relationEntity;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.compositeKey.ClassifiedExerciseKey;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "classified_exercise")
public class ClassifiedExerciseEntity {
    @EmbeddedId
    ClassifiedExerciseKey id;

    @ManyToOne
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id")
    ExerciseEntity exercise;

    @ManyToOne
    @MapsId("exerciseTypeId")
    @JoinColumn(name = "exercise_type_id")
    ExerciseTypeEntity exerciseType;
}
