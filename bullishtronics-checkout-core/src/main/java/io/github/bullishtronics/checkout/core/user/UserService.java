package io.github.bullishtronics.checkout.core.user;

import io.github.bullishtronics.checkout.models.user.CreateUserDetails;
import io.github.bullishtronics.checkout.models.user.User;

import java.util.List;

public interface UserService {
    void register(CreateUserDetails createUserDetails, User requester);

    User getByUsername(String username);

    List<User> getAll();
}
