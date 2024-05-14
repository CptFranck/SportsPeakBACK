package com.CptFranck.SportsPeak.domain.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "muscle")
public class MuscleEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "muscle_id_seq")
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "function", columnDefinition = "TEXT", nullable = false)
    private String function;
}
