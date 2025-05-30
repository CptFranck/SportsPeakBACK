package com.CptFranck.SportsPeak.unit.services.managers;

import com.CptFranck.SportsPeak.domain.entity.ExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.ProgExerciseEntity;
import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.user.InputUserRoles;
import com.CptFranck.SportsPeak.service.ProgExerciseService;
import com.CptFranck.SportsPeak.service.RoleService;
import com.CptFranck.SportsPeak.service.UserService;
import com.CptFranck.SportsPeak.service.managerImpl.UserManagerImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static com.CptFranck.SportsPeak.utils.ExerciseTestUtils.createTestExercise;
import static com.CptFranck.SportsPeak.utils.ProgExerciseTestUtils.createTestProgExercise;
import static com.CptFranck.SportsPeak.utils.RoleTestUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestInputUserRoles;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.createTestUser;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserManagerImplTest {

    @InjectMocks
    private UserManagerImpl userManager;

    @Mock
    private UserService userService;

    @Mock
    private ProgExerciseService progExerciseService;

    @Mock
    private RoleService roleService;

    @Test
    void saveProgExercise_AddNewRole_ReturnRoleEntity() {
        UserEntity user = createTestUser(1L);
        ExerciseEntity exercise = createTestExercise(1L);
        ProgExerciseEntity progExercise = createTestProgExercise(null, user, exercise);
        when(progExerciseService.save(Mockito.any(ProgExerciseEntity.class))).thenReturn(progExercise);

        ProgExerciseEntity roleSaved = userManager.saveProgExercise(progExercise);

        Assertions.assertEquals(progExercise, roleSaved);
    }

    @Test
    void saveRole_AddNewRole_ReturnRoleEntity() {
        RoleEntity role = createTestRole(null, 0);
        when(roleService.save(Mockito.any(RoleEntity.class))).thenReturn(role);

        RoleEntity roleSaved = userManager.saveRole(role);

        Assertions.assertEquals(role, roleSaved);
    }

    @Test
    void saveRole_UpdateRole_ReturnRoleEntity() {
        RoleEntity role = createTestRole(1L, 0);
        when(roleService.findOne(Mockito.any(Long.class))).thenReturn(role);
        when(roleService.save(Mockito.any(RoleEntity.class))).thenReturn(role);

        RoleEntity roleSaved = userManager.saveRole(role);

        Assertions.assertEquals(role, roleSaved);
    }

    @Test
    void updateUserRoles_ValidUse_ReturnRoleEntity() {
        UserEntity user = createTestUser(1L);
        when(userService.findOne(Mockito.any(Long.class))).thenReturn(user);
        when(roleService.findMany(Mockito.anySet())).thenReturn(new HashSet<>());
        when(userService.save(Mockito.any(UserEntity.class))).thenReturn(user);
        InputUserRoles inputUserRoles = createTestInputUserRoles(user.getId());

        UserEntity userSaved = userManager.updateUserRoles(inputUserRoles);

        Assertions.assertEquals(user, userSaved);
    }
}
