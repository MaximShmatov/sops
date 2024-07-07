package org.sops.services.security;

import lombok.RequiredArgsConstructor;
import org.sops.database.entities.UserEntity;
import org.sops.dto.JwtAuthResponse;
import org.sops.dto.LoginRequest;
import org.sops.services.UserService;
import org.sops.types.RoleType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;

    public JwtAuthResponse register(LoginRequest request, String roleName) {
        var user = UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(RoleType.valueOf(roleName))
                .build();
        userService.addUser(user);
        return new JwtAuthResponse(jwtTokenService.generateToken(user));
    }

    public JwtAuthResponse login(LoginRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        authenticationProvider.authenticate(authToken);
        var user = userService.loadUserByUsername(request.username());
        return new JwtAuthResponse(jwtTokenService.generateToken(user));
    }

}
