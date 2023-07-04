package io.github.bullishtronics.checkout.app.security.impl;

import io.github.bullishtronics.checkout.core.user.UserService;
import io.github.bullishtronics.checkout.models.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.getByUsername(username);
        System.out.println("For this: " + username);
        System.out.println("Found this: " + user);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}