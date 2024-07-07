package org.sops.dto;

import jakarta.validation.constraints.NotNull;

public record StatisticDto(@NotNull int ready,
                           @NotNull int inProgress,
                           @NotNull int processed) {
}
