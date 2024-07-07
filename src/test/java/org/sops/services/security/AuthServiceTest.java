package org.sops.services.security;

import org.junit.jupiter.api.Test;
import org.sops.BaseTest;
import org.sops.database.entities.UserEntity;
import org.sops.dto.LoginRequest;
import org.sops.services.UserService;
import org.sops.types.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AuthServiceTest extends BaseTest {
    final LoginRequest loginRequest = new LoginRequest("User1", "Password1", "");
    @Autowired
    AuthService authService;
    @MockBean
    UserService userService;
    @MockBean
    PasswordEncoder passwordEncoder;
    @MockBean
    AuthenticationProvider authenticationProvider;

    @Test
    void register() {
        var jwtToken = authService.register(loginRequest, RoleType.POSTER.name());
        verify(userService, times(1)).addUser(any(UserEntity.class));
        verify(passwordEncoder, times(1)).encode("Password1");
        assertNotNull(jwtToken.token());
    }

    @Test
    void login() {
        when(userService.loadUserByUsername(anyString())).thenReturn(posterEntity);
        var jwtToken = authService.login(loginRequest);
        verify(authenticationProvider, times(1)).authenticate(any(Authentication.class));
        verify(userService, times(1)).loadUserByUsername("User1");
        assertNotNull(jwtToken.token());
    }

}