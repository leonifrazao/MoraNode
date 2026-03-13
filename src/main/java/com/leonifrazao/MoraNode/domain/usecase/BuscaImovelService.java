package com.leonifrazao.MoraNode.domain.usecase;

import com.leonifrazao.MoraNode.domain.model.ImovelDomain;
import com.leonifrazao.MoraNode.domain.port.in.BuscaImovelUseCase;
import com.leonifrazao.MoraNode.domain.port.out.ImovelRepositoryPort;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BuscaImovelService implements BuscaImovelUseCase {

    private final ImovelRepositoryPort repositoryPort;

    @Override
    public ImovelDomain buscaID(Long id, Long usuarioId) {
        return repositoryPort.buscarPorID(id, usuarioId);
    }

    @Override
    public List<ImovelDomain> buscar(Long usuarioId) {
        return repositoryPort.buscar(usuarioId);
    }

}
