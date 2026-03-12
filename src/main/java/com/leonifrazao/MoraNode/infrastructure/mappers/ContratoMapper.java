package com.leonifrazao.MoraNode.infrastructure.mappers;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;
import com.leonifrazao.MoraNode.infrastructure.database.entities.ContratoEntity;

public class ContratoMapper {
    public static ContratoDomain toDomain(ContratoEntity entity) {
        return new ContratoDomain(
                entity.getId(),
                entity.getImovelId(),
                entity.getNomeDono(),
                entity.getNomeInquilino(),
                entity.getValorAcordado(),
                entity.getDataInicio(),
                entity.getDataFim(),
                entity.getTaxaJurosMensal(),
                entity.getTipo(),
                entity.getStatusContrato()
        );
    }

    public static ContratoEntity toEntity(ContratoDomain domain) {
        return new ContratoEntity(
                domain.getId(),
                domain.getImovelId(),
                domain.getNomeDono(),
                domain.getNomeInquilino(),
                domain.getValorAcordado(),
                domain.getDataInicio(),
                domain.getDataFim(),
                domain.isPodeRenovar(),
                domain.getTaxaJurosMensal(),
                domain.getStatusContrato(),
                domain.getTipo()
        );
    }
}
