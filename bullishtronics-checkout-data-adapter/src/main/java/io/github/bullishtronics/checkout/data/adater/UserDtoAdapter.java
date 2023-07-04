package io.github.bullishtronics.checkout.data.adater;

import io.github.bullishtronics.checkout.io.user.RoleDto;
import io.github.bullishtronics.checkout.io.user.UserDto;
import io.github.bullishtronics.checkout.models.user.Role;
import io.github.bullishtronics.checkout.models.user.User;

public class UserDtoAdapter {
    public UserDto adapte(User user) {
        return new UserDto(
                user.getUsername(),
                getRole(user.getRole()),
                user.getAddress(),
                user.getUserFullName()
        );
    }

    private RoleDto getRole(Role role) {
        return switch (role) {
            case CUSTOMER -> RoleDto.CUSTOMER;
            case ADMIN -> RoleDto.ADMIN;
        };
    }
}
