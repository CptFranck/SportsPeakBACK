package com.CptFranck.SportsPeak.resolvers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.enumType.TrustLabel;
import com.CptFranck.SportsPeak.domain.enumType.VisibilityLabel;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UserNotFoundException;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputNewProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExercise;
import com.CptFranck.SportsPeak.domain.input.progExercise.InputProgExerciseTrustLabel;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import com.CptFranck.SportsPeak.service.UserService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.CptFranck.SportsPeak.domain.enumType.VisibilityLabel.valueOfLabel;

@Component
public class ProgExerciseInputResolver {

    private final UserService userService;

    private final ExerciseService exerciseService;

    private final ProgExerciseService progExerciseService;

    public ProgExerciseInputResolver(UserService userService, ExerciseService exerciseService, ProgExerciseService progExerciseService) {
        this.userService = userService;
        this.exerciseService = exerciseService;
        this.progExerciseService = progExerciseService;
    }

    public ProgExerciseEntity resolveInput(InputNewProgExercise inputNewProgExercise) {

        UserEntity creator = userService.findOne(inputNewProgExercise.getCreatorId()).orElseThrow(
                () -> new UserNotFoundException(inputNewProgExercise.getCreatorId()));
        ExerciseEntity exercise = exerciseService.findOne(inputNewProgExercise.getExerciseId());

        Set<TargetSetEntity> targetSets = new HashSet<>();
        Set<UserEntity> subscribedUsers = new HashSet<>();

        return new ProgExerciseEntity(
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
    }

    public ProgExerciseEntity resolveInput(InputProgExercise inputProgExercise) {

        ProgExerciseEntity progExercise = progExerciseService.findOne(inputProgExercise.getId());
        ExerciseEntity newExercise = exerciseService.findOne(inputProgExercise.getExerciseId());

        progExercise.setNote(inputProgExercise.getNote());
        progExercise.setName(inputProgExercise.getName());
        progExercise.setVisibility(VisibilityLabel.valueOfLabel(inputProgExercise.getVisibility()));
        progExercise.setExercise(newExercise);

        return progExercise;
    }

    public ProgExerciseEntity resolveInput(InputProgExerciseTrustLabel inputProgExerciseTrustLabel) {

        ProgExerciseEntity progExercise = progExerciseService.findOne(inputProgExerciseTrustLabel.getId());
        progExercise.setTrustLabel(TrustLabel.valueOfLabel(inputProgExerciseTrustLabel.getTrustLabel()));

        return progExercise;
    }
}
