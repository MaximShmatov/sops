package org.sops.services;

import org.junit.jupiter.api.Test;
import org.sops.BaseTest;
import org.sops.database.entities.UserEntity;
import org.sops.database.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest extends BaseTest {
    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Test
    void loadUserByUsername() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(posterEntity));
        var user = userService.loadUserByUsername("any");
        assertNotNull(user);
        assertEquals(user.getUsername(), "User1");
    }

    @Test
    void addUser() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(posterEntity);
        var user = userService.addUser(posterEntity);
        assertNotNull(user);
        assertEquals(user.getUsername(), "User1");
    }
}