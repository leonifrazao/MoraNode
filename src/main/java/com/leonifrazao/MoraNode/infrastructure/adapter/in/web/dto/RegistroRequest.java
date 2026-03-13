package com.leonifrazao.MoraNode.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
        @NotBlank(message = "O nome é obrigatório!")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres!")
        String nome,

        @NotBlank(message = "O email é obrigatório!")
        @Email(message = "Email inválido!")
        @Size(max = 100, message = "O email deve ter no máximo 100 caracteres!")
        String email,

        @NotBlank(message = "A senha é obrigatória!")
        @Size(min = 6, max = 50, message = "A senha deve ter entre 6 e 50 caracteres!")
        String senha
) {}
