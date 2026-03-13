package com.leonifrazao.MoraNode.domain.port.in;

import com.leonifrazao.MoraNode.domain.model.ImovelDomain;

import java.util.List;

public interface BuscaImovelUseCase {
    ImovelDomain buscaID(Long id, Long usuarioId);
    List<ImovelDomain> buscar(Long usuarioId);
}
