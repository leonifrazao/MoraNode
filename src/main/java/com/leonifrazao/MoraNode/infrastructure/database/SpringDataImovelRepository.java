package com.leonifrazao.MoraNode.infrastructure.database;

import com.leonifrazao.MoraNode.infrastructure.database.entities.ImovelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataImovelRepository extends JpaRepository<ImovelEntity, Long> {
    List<ImovelEntity> findByUsuarioId(Long usuarioId);
    Optional<ImovelEntity> findByIdAndUsuarioId(Long id, Long usuarioId);
}
