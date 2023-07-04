package io.github.bullishtronics.checkout.core.user.impl;

import io.github.bullishtronics.checkout.core.user.StringEncrypter;
import io.github.bullishtronics.checkout.core.user.UserService;
import io.github.bullishtronics.checkout.core.user.UserStore;
import io.github.bullishtronics.checkout.core.user.exception.UserActionException;
import io.github.bullishtronics.checkout.models.user.CreateUserDetails;
import io.github.bullishtronics.checkout.models.user.Role;
import io.github.bullishtronics.checkout.models.user.User;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserStore userStore;
    private final StringEncrypter stringEncrypter;

    private final Integer DEFAULT_MIN_PASSWORD_LENGTH = 4;

    public UserServiceImpl(UserStore userStore, StringEncrypter stringEncrypter) {
        this.userStore = userStore;
        this.stringEncrypter = stringEncrypter;
    }

    @Override
    public void register(CreateUserDetails createUserDetails, User requester) {
        validate(createUserDetails, requester);

        this.userStore.save(new User(
                createUserDetails.getUsername(),
                stringEncrypter.encrypt(createUserDetails.getPassword()),
                createUserDetails.getRole(),
                createUserDetails.getAddress(),
                createUserDetails.getUserFullName()
        ));
    }

    @Override
    public User getByUsername(String username) {
        return this.userStore.getByUsername(username);
    }

    @Override
    public List<User> getAll() {
        return this.userStore.getAll();
    }

    private void validate(CreateUserDetails createUserDetails, User requester) {
        if (requester.getRole() != Role.ADMIN) {
            throw new UserActionException("Only admin can register new users.");
        }else if(createUserDetails.getUsername() == null){
            throw new UserActionException("Username is required.");
        }else if(createUserDetails.getPassword() == null) {
            throw new UserActionException("Password is required.");
        } else if(createUserDetails.getPassword().length() < DEFAULT_MIN_PASSWORD_LENGTH){
            throw new UserActionException("Password must be at least " + DEFAULT_MIN_PASSWORD_LENGTH + " characters long.");
        }else if (createUserDetails.getRole() == null) {
            throw new UserActionException("Role is required.");
        } else if (createUserDetails.getUserFullName() == null) {
            throw new UserActionException("User full name is required.");
        } else if (userStore.getByUsername(createUserDetails.getUsername()) != null) {
            throw new UserActionException("Username already exists.");
        }
    }
}
