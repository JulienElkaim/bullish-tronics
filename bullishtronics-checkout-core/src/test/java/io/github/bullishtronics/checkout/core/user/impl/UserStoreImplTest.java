package io.github.bullishtronics.checkout.core.user.impl;

import io.github.bullishtronics.checkout.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserStoreImplTest {
    private UserStoreImpl userStore;

    private final String username = "username";
    private final String username2 = "username2";
    private final User user = mock(User.class);
    private final User user2 = mock(User.class);

    @BeforeEach
    void init(){
        when(user.getUsername()).thenReturn(username);
        when(user2.getUsername()).thenReturn(username2);

        userStore = new UserStoreImpl(Map.of(username, user));
    }

    @Test
    void save_thenStored(){
        assertNull(userStore.getByUsername(username2));
        userStore.save(user2);
        assertSame(user2, userStore.getByUsername(username2));
    }

    @Test
    void save_andAlreadyHaveARecordMapped_thenUpdateRecord(){
        when(user2.getUsername()).thenReturn(username);
        assertSame(user, userStore.getByUsername(username));

        userStore.save(user2);

        assertSame(user2, userStore.getByUsername(username));
    }

    @Test
    void getByUsername_thenReturnStoredRecord(){
        User result = userStore.getByUsername(username);
        assertSame(user, result);
    }

    @Test
    void getByUsername_butRecordNotStored_thenReturnNull(){
        User result = userStore.getByUsername(username2);
        assertNull(result);
    }

    @Test
    void getAll_thenReturnListOfStoredRecords(){
        List<User> result = userStore.getAll();
        assertEquals(1, result.size());
        assertSame(user, result.get(0));

        reInitStore(Map.of(username, user, username2, user2));

        List<User> result2 = userStore.getAll();
        assertEquals(2, result2.size());
        assertTrue(result2.contains(user));
        assertTrue(result2.contains(user2));
    }

    @Test
    void getAll_butNoRecordStored_thenReturnEmptyList(){
        reInitStore(Map.of());
        List<User> result = userStore.getAll();
        assertTrue(result.isEmpty());
    }

    private void reInitStore(Map<String, User> defaultCache){
        userStore = new UserStoreImpl(defaultCache);
    }

}