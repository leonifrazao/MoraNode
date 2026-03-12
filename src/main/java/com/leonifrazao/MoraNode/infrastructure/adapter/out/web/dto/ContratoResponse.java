package com.leonifrazao.MoraNode.infrastructure.adapter.out.web.dto;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;
import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;
import com.leonifrazao.MoraNode.domain.model.enums.TipoContrato;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratoResponse(
        Long id,
        Long imovelId,
        String nomeInquilino,
        BigDecimal valorAcordado,
        LocalDate dataInicio,
        LocalDate dataFim,
        StatusContrato statusContrato,
        TipoContrato tipo,
        boolean podeRenovar
) {
    public static ContratoResponse fromDomain(ContratoDomain dominio) {
        return new ContratoResponse(
                dominio.getId(),
                dominio.getImovelId(),
                dominio.getNomeInquilino(),
                dominio.getValorAcordado(),
                dominio.getDataInicio(),
                dominio.getDataFim(),
                dominio.getStatusContrato(),
                dominio.getTipo(),
                dominio.isPodeRenovar()
        );
    }
}