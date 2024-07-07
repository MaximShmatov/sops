package org.sops.dto;

import jakarta.validation.constraints.NotBlank;

public record PlaceOrderRequest(@NotBlank String title, String description) {
}
