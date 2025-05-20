package com.CptFranck.SportsPeak.resolvers;

import com.CptFranck.SportsPeak.domain.entity.UserEntity;
import com.CptFranck.SportsPeak.domain.input.user.AbstractInputUser;
import com.CptFranck.SportsPeak.domain.input.user.InputUserIdentity;
import com.CptFranck.SportsPeak.domain.input.user.InputUserUsername;
import com.CptFranck.SportsPeak.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserInputResolver {

    private final UserService userService;

    public UserInputResolver(UserService userService) {
        this.userService = userService;
    }

    public UserEntity resolveInput(AbstractInputUser input) {
        UserEntity user = userService.findOne(input.getId());

        if (input instanceof InputUserUsername inputUserUsername) {
            user.setUsername(inputUserUsername.getNewUsername());
        }
        if (input instanceof InputUserIdentity inputUserIdentity) {
            user.setFirstName(inputUserIdentity.getFirstName());
            user.setLastName(inputUserIdentity.getLastName());
        }

        return user;
    }
}
