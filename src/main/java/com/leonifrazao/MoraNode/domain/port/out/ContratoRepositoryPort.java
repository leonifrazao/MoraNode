package com.leonifrazao.MoraNode.domain.port.out;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;

import java.util.List;

public interface ContratoRepositoryPort {
    List<ContratoDomain> buscar();
    void salvar(ContratoDomain dominio);
}
