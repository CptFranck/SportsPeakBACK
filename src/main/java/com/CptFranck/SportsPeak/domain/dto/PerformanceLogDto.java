package com.CptFranck.SportsPeak.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceLogDto {
    private Long id;

    private Integer setIndex;

    private Integer repetitionNumber;

    private Float weight;

    private String weightUnit;

    private LocalDateTime logDate;

    private TargetSetDto targetSet;
}
