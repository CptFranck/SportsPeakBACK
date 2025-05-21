package com.CptFranck.SportsPeak.service.managerImpl;

import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.TargetSetEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.progExercise.ProgExerciseStillUsedException;
import com.CptFranck.SportsPeak.service.ProgExerciseManager;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import com.CptFranck.SportsPeak.service.TargetSetService;
import com.CptFranck.SportsPeak.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class ProgExerciseManagerImpl implements ProgExerciseManager {

    private final UserService userService;

    private final TargetSetService targetSetService;

    private final ProgExerciseService progExerciseService;

    public ProgExerciseManagerImpl(UserService userService, TargetSetService targetSetService, ProgExerciseService progExerciseService) {
        this.userService = userService;
        this.targetSetService = targetSetService;
        this.progExerciseService = progExerciseService;
    }

    @Override
    public TargetSetEntity saveTargetSet(TargetSetEntity targetSet, Long targetSetUpdateId) {
        TargetSetEntity targetSetSaved = targetSetService.save(targetSet);

        ProgExerciseEntity progExercise = targetSet.getProgExercise();
        progExercise.getTargetSets().add(targetSet);
        progExerciseService.save(progExercise);

        if (targetSetUpdateId != null) {
            TargetSetEntity targetSetUpdated = targetSetService.findOne(targetSetUpdateId);
            targetSetUpdated.setTargetSetUpdate(targetSet);
            targetSetService.save(targetSetUpdated);
        }

        return targetSetSaved;
    }

    @Override
    public void deleteProgExercise(Long id) {
        ProgExerciseEntity progExercise = progExerciseService.findOne(id);
        Set<UserEntity> users = userService.findUserBySubscribedProgExercises(progExercise);

        boolean usersContainsCreator = users.stream().anyMatch(user -> Objects.equals(user.getId(), progExercise.getCreator().getId()));
        if (users.isEmpty() || users.size() == 1 && usersContainsCreator) {
            if (usersContainsCreator) {
                UserEntity user = userService.findOne(progExercise.getCreator().getId());
                user.getSubscribedProgExercises().removeIf(prgEx -> progExercise.getId().equals(prgEx.getId()));
                userService.save(user);
            }
            progExerciseService.delete(id);
        } else {
            throw new ProgExerciseStillUsedException(id);
        }
    }
}
