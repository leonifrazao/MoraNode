package com.leonifrazao.MoraNode.infrastructure.database;

import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;
import com.leonifrazao.MoraNode.infrastructure.database.entities.ContratoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataContratoRepository extends JpaRepository<ContratoEntity, Long> {
    List<ContratoEntity> findByImovelId(Long imovelId);

    boolean existsByImovelIdAndStatusContratoIn(Long imovelId, List<StatusContrato> statuses);
}
