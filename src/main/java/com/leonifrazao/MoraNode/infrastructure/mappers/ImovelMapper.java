package com.leonifrazao.MoraNode.infrastructure.mappers;

import com.leonifrazao.MoraNode.domain.model.ImovelDomain;
import com.leonifrazao.MoraNode.infrastructure.database.entities.ImovelEntity;

public class ImovelMapper {

    public static ImovelEntity fromDomain(ImovelDomain domain) {
        if (domain == null) return null;

        ImovelEntity entity = new ImovelEntity();
        entity.setId(domain.getId());
        entity.setValor(domain.getValor());
        entity.setEndereco(domain.getEndereco());
        entity.setMetrosQuadrados(domain.getMetrosQuadrados());
        entity.setDisponivel(domain.isDisponivel());
        return entity;
    }

    public static ImovelDomain toDomain(ImovelEntity entidade) {
        if (entidade == null) return null;
        return new ImovelDomain(
                entidade.getId(),
                entidade.getValor(),
                entidade.isDisponivel(),
                entidade.getMetrosQuadrados(),
                entidade.getEndereco()
        );
    }
}
