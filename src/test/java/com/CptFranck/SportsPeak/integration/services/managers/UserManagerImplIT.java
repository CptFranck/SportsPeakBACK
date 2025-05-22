package com.CptFranck.SportsPeak.integration.services.managers;

import com.CptFranck.SportsPeak.domain.entity.RoleEntity;
import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.exception.role.RoleExistsException;
import com.CptFranck.SportsPeak.domain.exception.role.RoleNotFoundException;
import com.CptFranck.SportsPeak.domain.exception.userAuth.UserNotFoundException;
import com.CptFranck.SportsPeak.domain.input.user.InputUserRoles;
import com.CptFranck.SportsPeak.repository.RoleRepository;
import com.CptFranck.SportsPeak.repository.UserRepository;
import com.CptFranck.SportsPeak.service.managerImpl.UserManagerImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Set;

import static com.CptFranck.SportsPeak.utils.RoleTestUtils.assertEqualsRole;
import static com.CptFranck.SportsPeak.utils.RoleTestUtils.createTestRole;
import static com.CptFranck.SportsPeak.utils.UserTestUtils.*;

@SpringBootTest()
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class UserManagerImplIT {

    @Autowired
    private UserManagerImpl userManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void saveRole_AddNewRole_ReturnRoleEntity() {
        RoleEntity role = createTestRole(null, 0);

        RoleEntity roleResolved = userManager.saveRole(role);

        Assertions.assertEquals(role, roleResolved);
    }

    @Test
    void saveRole_InvalidRoleId_ThrowRoleNotFoundException() {
        RoleEntity role = createTestRole(1L, 0);

        Assertions.assertThrows(RoleNotFoundException.class, () -> userManager.saveRole(role));
    }

    @Test
    void saveRole_InvalidRoleId_ThrowRoleExistsException() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));
        RoleEntity roleBis = roleRepository.save(createTestRole(null, 1));
        role.setName(roleBis.getName());

        Assertions.assertThrows(RoleExistsException.class, () -> userManager.saveRole(role));
    }

    @Test
    void saveRole_UpdateRole_ReturnRoleEntity() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));
        UserEntity user = userRepository.save(createTestUser(null));
        user.getRoles().add(role);
        user = userRepository.save(user);
        UserEntity userBis = userRepository.save(createTestUserBis(null));
        role.setUsers(Set.of(userBis));

        RoleEntity roleResolved = userManager.saveRole(role);

        assertEqualsRole(role, roleResolved);
    }

    @Test
    void updateUserRoles_ValidUse_ReturnRoleEntity() {
        InputUserRoles inputUserRoles = createTestInputUserRoles(1L);

        Assertions.assertThrows(UserNotFoundException.class, () -> userManager.updateUserRoles(inputUserRoles));
    }

    @Test
    void updateUserRoles_InvalidUse_ThrowRoleNotFoundException() {
        RoleEntity role = roleRepository.save(createTestRole(null, 0));
        RoleEntity roleBis = roleRepository.save(createTestRole(null, 1));
        UserEntity user = userRepository.save(createTestUser(null));
        user.getRoles().add(role);
        user = userRepository.save(user);
        InputUserRoles inputUserRoles = createTestInputUserRoles(user.getId());
        inputUserRoles.getRoleIds().add(roleBis.getId());

        UserEntity userResolved = userManager.updateUserRoles(inputUserRoles);

        assertEqualsUser(user, userResolved, false);
    }
}
