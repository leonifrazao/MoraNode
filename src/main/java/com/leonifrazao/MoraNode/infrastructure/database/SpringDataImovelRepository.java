package com.leonifrazao.MoraNode.infrastructure.database;

import com.leonifrazao.MoraNode.infrastructure.database.entities.ImovelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataImovelRepository extends JpaRepository<ImovelEntity, Long> {
}
