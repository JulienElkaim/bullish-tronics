package io.github.bullishtronics.checkout.core.user.impl;

import io.github.bullishtronics.checkout.core.user.StringEncrypter;
import io.github.bullishtronics.checkout.core.user.UserStore;
import io.github.bullishtronics.checkout.core.user.exception.UserActionException;
import io.github.bullishtronics.checkout.models.user.CreateUserDetails;
import io.github.bullishtronics.checkout.models.user.Role;
import io.github.bullishtronics.checkout.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    private final UserStore userStore = mock(UserStore.class);
    private final StringEncrypter stringEncrypter = mock(StringEncrypter.class);
    private final CreateUserDetails userDetails = mock(CreateUserDetails.class);
    private final User requester = mock(User.class);

    private final String username = "username";
    private final String password = "password";
    private final String passwordEncrypted = "passwordEncrypted";
    private final String userFullName = "userFullName";
    private final String address = "address";

    private ArgumentCaptor<User> argumentCaptor;
    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        userService = new UserServiceImpl(userStore, stringEncrypter);
        argumentCaptor = ArgumentCaptor.forClass(User.class);

        when(requester.getRole()).thenReturn(Role.ADMIN);

        when(userDetails.getUsername()).thenReturn(username);
        when(userDetails.getPassword()).thenReturn(password);
        when(userDetails.getRole()).thenReturn(Role.ADMIN);
        when(userDetails.getUserFullName()).thenReturn(userFullName);
        when(userDetails.getAddress()).thenReturn(address);

        when(stringEncrypter.encrypt(password)).thenReturn(passwordEncrypted);
    }

    @Test
    void register_thenSuccess(){
        userService.register(userDetails, requester);

        verify(userStore).save(argumentCaptor.capture());
        User stored = argumentCaptor.getValue();

        assertEquals(username, stored.getUsername());
        assertEquals(passwordEncrypted, stored.getPassword());
        assertEquals(Role.ADMIN, stored.getRole());
        assertEquals(address, stored.getAddress());
        assertEquals(userFullName, stored.getUserFullName());
    }

    @Test
    void register_butRequesterIsNotAdmin_thenThrowException(){
        when(requester.getRole()).thenReturn(Role.CUSTOMER);

        UserActionException exception = assertThrows(UserActionException.class, () -> userService.register(userDetails, requester));

        verify(userStore, never()).save(any());
        assertEquals("Only admin can register new users.", exception.getMessage());
    }

    @Test
    void register_butUserDetailsUsernameIsNull_thenThrowException(){
        when(userDetails.getUsername()).thenReturn(null);

        UserActionException exception = assertThrows(UserActionException.class, () -> userService.register(userDetails, requester));

        verify(userStore, never()).save(any());
        assertEquals("Username is required.", exception.getMessage());
    }

    @Test
    void register_butUserDetailsPasswordIsNull_thenThrowException(){
        when(userDetails.getPassword()).thenReturn(null);

        UserActionException exception = assertThrows(UserActionException.class, () -> userService.register(userDetails, requester));

        verify(userStore, never()).save(any());
        assertEquals("Password is required.", exception.getMessage());
    }

    @Test
    void register_butUserDetailsPasswordIsTooShort_thenThrowException(){
        when(userDetails.getPassword()).thenReturn("abc");

        UserActionException exception = assertThrows(UserActionException.class, () -> userService.register(userDetails, requester));

        verify(userStore, never()).save(any());
        assertEquals("Password must be at least 4 characters long.", exception.getMessage());
    }

    @Test
    void register_butUserDetailsRoleIsNull_thenThrowException(){
        when(userDetails.getRole()).thenReturn(null);

        UserActionException exception = assertThrows(UserActionException.class, () -> userService.register(userDetails, requester));

        verify(userStore, never()).save(any());
        assertEquals("Role is required.", exception.getMessage());
    }

    @Test
    void register_butUserDetailsUserFullnameIsNull_thenThrowException(){
        when(userDetails.getUserFullName()).thenReturn(null);

        UserActionException exception = assertThrows(UserActionException.class, () -> userService.register(userDetails, requester));

        verify(userStore, never()).save(any());
        assertEquals("User full name is required.", exception.getMessage());
    }

    @Test
    void register_butUserDetailsUsernameIsAlreadyPresentInStore_thenThrowException(){
        when(userStore.getByUsername(username)).thenReturn(mock(User.class));

        UserActionException exception = assertThrows(UserActionException.class, () -> userService.register(userDetails, requester));

        verify(userStore, never()).save(any());
        assertEquals("Username already exists.", exception.getMessage());
    }

    @Test
    void getByUsername_delegatesToUserStore(){
        when(userStore.getByUsername(username)).thenReturn(requester);
        User result = userService.getByUsername(username);
        assertSame(requester, result);
        verify(userStore).getByUsername(username);
    }

    @Test
    void getAll_delegatesToUserStore(){
        when(userStore.getAll()).thenReturn(List.of(requester));
        List<User> result = userService.getAll();

        verify(userStore).getAll();
        assertEquals(1, result.size());
        assertSame(requester, result.get(0));
    }
}