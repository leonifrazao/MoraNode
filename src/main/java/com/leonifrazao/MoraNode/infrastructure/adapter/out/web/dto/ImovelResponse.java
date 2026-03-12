package com.leonifrazao.MoraNode.infrastructure.adapter.out.web.dto;

import com.leonifrazao.MoraNode.domain.model.ImovelDomain;

public record ImovelResponse(Long id, int valor, int metrosQuadrados, boolean disponivel) {
    public static ImovelResponse fromDomain (ImovelDomain dominio) {
        return new ImovelResponse(
                dominio.getId(),
                dominio.getValor(),
                dominio.getMetrosQuadrados(),
                dominio.isDisponivel()
        );
    }
}
