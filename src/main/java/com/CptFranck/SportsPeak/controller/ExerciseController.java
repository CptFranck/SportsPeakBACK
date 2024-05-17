package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.ExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ExerciseTypeEntity;
import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.domain.input.InputNewExercise;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.MuscleService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@DgsComponent
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ExerciseTypeService exerciseTypeService;
    private final MuscleService muscleService;
    private final Mapper<ExerciseEntity, ExerciseDto> exerciseMapper;

    public ExerciseController(ExerciseService exerciseService, ExerciseTypeService exerciseTypeService, MuscleService muscleService, Mapper<ExerciseEntity, ExerciseDto> exerciseMapper) {
        this.exerciseService = exerciseService;
        this.exerciseTypeService = exerciseTypeService;
        this.muscleService = muscleService;
        this.exerciseMapper = exerciseMapper;
    }

    @DgsQuery
    public List<ExerciseDto> getExercises() {
        return exerciseService.findAll().stream().map(exerciseMapper::mapTo).toList();
    }

    @DgsQuery
    public ExerciseDto getExerciseById(@InputArgument Integer id) {
        Optional<ExerciseEntity> exerciseEntity = exerciseService.findOne(id.longValue());
        return exerciseEntity.map(exerciseMapper::mapTo).orElse(null);
    }

    @DgsMutation
    public ExerciseDto addExercise(@InputArgument InputNewExercise inputNewExercise) {
        System.out.println(inputNewExercise.getName());
        System.out.println(inputNewExercise.getGoal());
        System.out.println(inputNewExercise.getDescription());
        System.out.println(inputNewExercise.getMuscleIds());
        System.out.println(inputNewExercise.getExerciseTypeIds());
        Set<Long> muscleIds = inputNewExercise.getExerciseTypeIds().stream()
                .map(Integer::longValue)
                .collect(Collectors.toSet());
        ;
        Set<Long> exerciseTypeIds = inputNewExercise.getExerciseTypeIds().stream()
                .map(Integer::longValue)
                .collect(Collectors.toSet());

        Set<MuscleEntity> muscles = muscleService.findMany(muscleIds);
        Set<ExerciseTypeEntity> exerciseTypes = exerciseTypeService.findMany(exerciseTypeIds);

        ExerciseEntity exercise = new ExerciseEntity(
                null,
                inputNewExercise.getName(),
                inputNewExercise.getDescription(),
                inputNewExercise.getGoal(),
                exerciseTypes,
                muscles
        );
        return exerciseMapper.mapTo(exerciseService.save(exercise));
    }

//    @DgsMutation
//    public ExerciseDto modifyExercise (@InputArgument InputExercise inputExercise) {
//
//        return exerciseEntity.map(exerciseMapper::mapTo).orElse(null);
//    }

//    @DgsMutation
//    public Integer deleteExercise (@InputArgument Integer exerciseId) {
//        if(!exerciseService.exists(exerciseId.longValue())){
//            return null;
//        }
//        exerciseService.delete(exerciseId.longValue());
//        return exerciseId;
//    }
}
