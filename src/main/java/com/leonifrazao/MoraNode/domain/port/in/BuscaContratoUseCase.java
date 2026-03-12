package com.leonifrazao.MoraNode.domain.port.in;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;

import java.util.List;

public interface BuscaContratoUseCase {
    List<ContratoDomain> buscar();
}
