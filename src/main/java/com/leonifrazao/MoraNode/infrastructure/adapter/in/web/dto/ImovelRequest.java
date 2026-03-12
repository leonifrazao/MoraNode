package com.leonifrazao.MoraNode.infrastructure.adapter.in.web.dto;

import com.leonifrazao.MoraNode.domain.model.ImovelDomain;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ImovelRequest (
        @Positive(message = "O valor deve ser maior que 0!")
        int valor,

        @NotBlank(message = "O Endereço é obrigatorio!")
        String endereco,

        @Positive(message = "A metragem deve ser positiva!")
        int metrosQuadrados,

        boolean disponivel
) {
        public ImovelDomain toDomain() {
                return new ImovelDomain(
                        this.valor,
                        this.endereco,
                        this.metrosQuadrados,
                        this.disponivel
                );
        }
}
