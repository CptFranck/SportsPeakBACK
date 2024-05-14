package com.CptFranck.SportsPeak.domain.entity.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class SolicitedMuscleKey implements Serializable {
    @Column(name = "exercise_id")
    Long exerciseId;

    @Column(name = "muscle_id")
    Long muscleId;
}
