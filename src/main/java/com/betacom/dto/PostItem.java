package com.betacom.dto;

import jakarta.validation.constraints.NotBlank;

public record PostItem(@NotBlank String title) {
}
