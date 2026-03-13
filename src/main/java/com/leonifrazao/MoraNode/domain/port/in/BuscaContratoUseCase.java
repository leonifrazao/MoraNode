package com.leonifrazao.MoraNode.domain.port.in;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;

import java.util.List;

public interface BuscaContratoUseCase {
    List<ContratoDomain> buscar(Long usuarioId);
    ContratoDomain buscarPorId(Long id, Long usuarioId);
    List<ContratoDomain> buscarPorImovelId(Long id, Long usuarioId);
}
