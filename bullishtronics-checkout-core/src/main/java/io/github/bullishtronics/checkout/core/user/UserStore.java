package io.github.bullishtronics.checkout.core.user;

import io.github.bullishtronics.checkout.models.user.User;

import java.util.List;

public interface UserStore {
    void save(User user);

    User getByUsername(String username);

    List<User> getAll();
}
