package io.github.bullishtronics.checkout.app.web;


import io.github.bullishtronics.checkout.app.security.BasicAuthBased;
import io.github.bullishtronics.checkout.app.security.OnlyAdminEndpoint;
import io.github.bullishtronics.checkout.core.user.UserService;
import io.github.bullishtronics.checkout.data.adater.UserDtoAdapter;
import io.github.bullishtronics.checkout.io.user.UserDto;
import io.github.bullishtronics.checkout.models.user.CreateUserDetails;
import io.github.bullishtronics.checkout.models.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@BasicAuthBased
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserDtoAdapter userDtoAdapter;

    public UserController(UserService userService,
                          UserDtoAdapter userDtoAdapter) {
        this.userService = userService;
        this.userDtoAdapter = userDtoAdapter;
    }

    @OnlyAdminEndpoint
    @PostMapping(value = "/")
    public ResponseEntity<Void> register(@RequestBody CreateUserDetails userDetails) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User requester = userService.getByUsername(currentUsername);
        userService.register(userDetails, requester);

        return ResponseEntity.ok().build();
    }

    @OnlyAdminEndpoint
    @GetMapping(value = "/")
    public ResponseEntity<Collection<UserDto>> getAll() {
        List<UserDto> userDtos = userService.getAll().stream()
                .map(userDtoAdapter::adapte)
                .toList();

        return ResponseEntity.ok(userDtos);
    }

}
