package com.leonifrazao.MoraNode.infrastructure.database;

import com.leonifrazao.MoraNode.infrastructure.database.entities.ContratoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataContratoRepository extends JpaRepository<ContratoEntity, Long> {
}
