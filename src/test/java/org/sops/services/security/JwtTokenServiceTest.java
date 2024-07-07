package org.sops.services.security;

import org.junit.jupiter.api.Test;
import org.sops.BaseTest;
import org.sops.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class JwtTokenServiceTest extends BaseTest {
    @Value("${security.signing-key}")
    String jwtSigningKey;
    @Autowired
    JwtTokenService jwtTokenService;
    @MockBean
    UserService userService;

    @Test
    void getAuthToken() {
        when(userService.loadUserByUsername(any(String.class))).thenReturn(posterEntity);
        var authToken = jwtTokenService.getAuthToken("User1");
        assertNotNull(authToken.getName());
    }

    @Test
    void isValidToken() {
        when(userService.loadUserByUsername(any(String.class))).thenReturn(posterEntity);
        var jwtToken = jwtTokenService.generateToken(posterEntity);
        var climes = jwtTokenService.getClaimsFromToken(jwtToken);
        var isValid = jwtTokenService.isValidToken(jwtToken, climes);
        assertTrue(isValid);
        when(userService.loadUserByUsername(any(String.class))).thenReturn(processorEntity);
        isValid = jwtTokenService.isValidToken(jwtToken, climes);
        assertFalse(isValid);
    }

    @Test
    void generateToken() {
        var jwtToken = jwtTokenService.generateToken(posterEntity);
        var climes = jwtTokenService.getClaimsFromToken(jwtToken);
        assertEquals(climes.getSubject(), "User1");
    }

}