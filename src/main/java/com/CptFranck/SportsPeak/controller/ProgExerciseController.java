package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.dto.ProgExerciseDto;
import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.enumType.TrustLabel;
import com.CptFranck.SportsPeak.domain.enumType.VisibilityLabel;
import com.CptFranck.SportsPeak.domain.exception.exercise.ExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseStillUsedException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UserNotFoundException;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputNewProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExerciseTrustLabel;
import com.CptFranck.SportsPeak.mappers.Mapper;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import com.CptFranck.SportsPeak.service.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.CptFranck.SportsPeak.domain.enumType.VisibilityLabel.valueOfLabel;

@DgsComponent
public class ProgExerciseController {

    private final UserService userService;
    private final ExerciseService exerciseService;
    private final ProgExerciseService progExerciseService;
    private final Mapper<ProgExerciseEntity, ProgExerciseDto> progExerciseMapper;

    public ProgExerciseController(UserService userService, ExerciseService exerciseService, ProgExerciseService progExerciseService, Mapper<ProgExerciseEntity, ProgExerciseDto> progExerciseMapper) {
        this.userService = userService;
        this.exerciseService = exerciseService;
        this.progExerciseService = progExerciseService;
        this.progExerciseMapper = progExerciseMapper;
    }

    @DgsQuery
    public List<ProgExerciseDto> getProgExercises() {
        return progExerciseService.findAll().stream().map(progExerciseMapper::mapTo).toList();
    }

    @DgsQuery
    public ProgExerciseDto getProgExerciseById(@InputArgument Long id) {
        Optional<ProgExerciseEntity> progExercise = progExerciseService.findOne(id);
        return progExercise.map(progExerciseMapper::mapTo).orElse(null);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public ProgExerciseDto addProgExercise(@InputArgument InputNewProgExercise inputNewProgExercise) {
        return progExerciseMapper.mapTo(inputNewToEntity(inputNewProgExercise));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public ProgExerciseDto modifyProgExercise(@InputArgument InputProgExercise inputProgExercise) {
        return progExerciseMapper.mapTo(inputToEntity(inputProgExercise));
    }

    @PreAuthorize("hasRole('ROLE_STAFF')")
    @DgsMutation
    public ProgExerciseDto modifyProgExerciseTrustLabel(@InputArgument InputProgExerciseTrustLabel inputProgExerciseTrustLabel) {
        return progExerciseMapper.mapTo(inputTrustLabelToEntity(inputProgExerciseTrustLabel));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DgsMutation
    public Long deleteProgExercise(@InputArgument Long progExerciseId) {
        ProgExerciseEntity progExercise = progExerciseService.findOne(progExerciseId)
                .orElseThrow(() -> new ProgExerciseNotFoundException(progExerciseId));
        Set<UserEntity> users = userService.findUserBySubscribedProgExercises(progExercise);
        if (users.isEmpty() || users.size() == 1 && users.contains(progExercise.getCreator())) {
            progExerciseService.delete(progExerciseId);
        } else {
            throw new ProgExerciseStillUsedException(progExerciseId);
        }
        return progExerciseId;
    }

    private ProgExerciseEntity inputNewToEntity(InputNewProgExercise inputNewProgExercise) {
        UserEntity creator = userService.findOne(inputNewProgExercise.getCreatorId()).orElseThrow(
                () -> new UserNotFoundException(inputNewProgExercise.getCreatorId()));
        ExerciseEntity exercise = exerciseService.findOne(inputNewProgExercise.getExerciseId()).orElseThrow(
                () -> new ExerciseNotFoundException(inputNewProgExercise.getExerciseId()));

        Set<TargetSetEntity> targetSets = new HashSet<>();
        Set<UserEntity> subscribedUsers = new HashSet<>();

        ProgExerciseEntity progExerciseEntity = new ProgExerciseEntity(
                null,
                inputNewProgExercise.getName(),
                inputNewProgExercise.getNote(),
                valueOfLabel(inputNewProgExercise.getVisibility()),
                TrustLabel.UNVERIFIED,
                subscribedUsers,
                creator,
                exercise,
                targetSets
        );

        progExerciseEntity = progExerciseService.save(progExerciseEntity);
        userService.save(creator);
        exercise.getProgExercises().add(progExerciseEntity);
        exerciseService.save(exercise);
        return progExerciseEntity;
    }

    private ProgExerciseEntity inputToEntity(InputProgExercise inputProgExercise) {

        ProgExerciseEntity progExercise = progExerciseService.findOne(inputProgExercise.getId()).orElseThrow(
                () -> new ProgExerciseNotFoundException(inputProgExercise.getId()));
        ExerciseEntity newExercise = exerciseService.findOne(inputProgExercise.getExerciseId()).orElseThrow(
                () -> new ExerciseNotFoundException(inputProgExercise.getExerciseId()));
        ExerciseEntity oldExercise = progExercise.getExercise();

        progExercise.setNote(inputProgExercise.getNote());
        progExercise.setName(inputProgExercise.getName());
        progExercise.setVisibility(VisibilityLabel.valueOfLabel(inputProgExercise.getVisibility()));
        progExercise.setExercise(newExercise);

        exerciseService.updateProgExerciseRelation(newExercise, oldExercise, progExercise);

        return progExerciseService.save(progExercise);
    }

    private ProgExerciseEntity inputTrustLabelToEntity(InputProgExerciseTrustLabel inputProgExerciseTrustLabel) {
        ProgExerciseEntity progExercise = progExerciseService.findOne(inputProgExerciseTrustLabel.getId()).orElseThrow(
                () -> new ExerciseNotFoundException(inputProgExerciseTrustLabel.getId()));

        progExercise.setTrustLabel(TrustLabel.valueOfLabel(inputProgExerciseTrustLabel.getTrustLabel()));
        return progExerciseService.save(progExercise);
    }
}
