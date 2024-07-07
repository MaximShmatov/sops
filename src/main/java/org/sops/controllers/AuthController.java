package org.sops.controllers;

import lombok.RequiredArgsConstructor;
import org.sops.dto.JwtAuthResponse;
import org.sops.dto.LoginRequest;
import org.sops.services.security.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController extends BaseController {
    private final AuthService authService;

    @PostMapping("/register/{roleName}")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtAuthResponse register(@RequestBody @Validated LoginRequest request,
                                    @PathVariable String roleName) {
        return authService.register(request, roleName);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtAuthResponse login(@RequestBody @Validated LoginRequest request) {
        return authService.login(request);
    }

}
