package com.leonifrazao.MoraNode.infrastructure.adapter.in.web.dto;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;
import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;
import com.leonifrazao.MoraNode.domain.model.enums.TipoContrato;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratoRequest(
        @NotNull(message = "O ID do imóvel é obrigatório")
        Long imovelId,

        @NotBlank(message = "O nome do dono é obrigatório")
        String nomeDono,

        @NotBlank(message = "O nome do inquilino é obrigatório")
        String nomeInquilino,

        @NotNull(message = "O valor acordado é obrigatório")
        @Positive(message = "O valor deve ser positivo")
        BigDecimal valorAcordado,

        @NotNull(message = "A data de início é obrigatória")
        @FutureOrPresent(message = "A data de início não pode ser no passado")
        LocalDate dataInicio,

        LocalDate dataFim, // Pode ser null em vendas, por exemplo

        boolean podeRenovar,

        @NotNull(message = "A taxa de juros é obrigatória")
        @PositiveOrZero(message = "A taxa de juros não pode ser negativa")
        BigDecimal taxaJurosMensal,

        @NotNull(message = "O tipo do contrato é obrigatório")
        TipoContrato tipo,

        @NotNull(message = "O status do contrato é obrigatório")
        StatusContrato statusContrato
) {
    public ContratoDomain toDomain() {
        // Passamos null no ID porque ele será gerado pelo banco
        return new ContratoDomain(
                null,
                this.imovelId,
                this.nomeDono,
                this.nomeInquilino,
                this.valorAcordado,
                this.dataInicio,
                this.dataFim,
                this.podeRenovar,
                this.taxaJurosMensal,
                this.tipo,
                this.statusContrato
        );
    }
}