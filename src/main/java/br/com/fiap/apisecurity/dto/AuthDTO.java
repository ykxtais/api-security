package br.com.fiap.apisecurity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AuthDTO(
        @NotBlank String login,
        @NotBlank String senha
) {
}