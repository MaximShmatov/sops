package org.sops.dto;

import jakarta.validation.constraints.NotBlank;

public record JwtAuthResponse(@NotBlank String token) {
}
