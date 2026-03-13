package com.leonifrazao.MoraNode.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "O email é obrigatório!")
        @Email(message = "Email inválido!")
        @Size(max = 100, message = "O email deve ter no máximo 100 caracteres!")
        String email,

        @NotBlank(message = "A senha é obrigatória!")
        @Size(max = 50, message = "A senha deve ter no máximo 50 caracteres!")
        String senha
) {}
