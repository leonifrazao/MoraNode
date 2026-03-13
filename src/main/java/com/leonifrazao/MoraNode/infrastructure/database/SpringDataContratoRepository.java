package com.leonifrazao.MoraNode.infrastructure.database;

import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;
import com.leonifrazao.MoraNode.infrastructure.database.entities.ContratoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface SpringDataContratoRepository extends JpaRepository<ContratoEntity, Long> {
    List<ContratoEntity> findByUsuarioId(Long usuarioId);
    Optional<ContratoEntity> findByIdAndUsuarioId(Long id, Long usuarioId);
    List<ContratoEntity> findByImovelIdAndUsuarioId(Long imovelId, Long usuarioId);

    boolean existsByImovelIdAndUsuarioIdAndStatusContratoIn(Long imovelId, Long usuarioId, List<StatusContrato> statuses);
}
