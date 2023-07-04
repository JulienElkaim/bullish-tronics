package io.github.bullishtronics.checkout.core.user.impl;

import io.github.bullishtronics.checkout.core.user.UserStore;
import io.github.bullishtronics.checkout.models.user.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserStoreImpl implements UserStore {
    private final ConcurrentHashMap<String, User> userMap;

    public UserStoreImpl(Map<String, User> usersInDatabaseAtStart) {
        this.userMap = new ConcurrentHashMap<>(usersInDatabaseAtStart);
    }

    @Override
    public void save(User user) {
        this.userMap.put(user.getUsername(), user);
    }

    @Override
    public User getByUsername(String username) {
        return this.userMap.get(username);
    }

    @Override
    public List<User> getAll() {
        return this.userMap.values().stream().toList();
    }
}
