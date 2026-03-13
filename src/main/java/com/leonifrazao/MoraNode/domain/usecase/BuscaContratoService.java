package com.leonifrazao.MoraNode.domain.usecase;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;
import com.leonifrazao.MoraNode.domain.port.in.BuscaContratoUseCase;
import com.leonifrazao.MoraNode.domain.port.out.ContratoRepositoryPort;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BuscaContratoService implements BuscaContratoUseCase {

    private final ContratoRepositoryPort repositoryPort;

    @Override
    public List<ContratoDomain> buscar(Long usuarioId) {
        return repositoryPort.buscar(usuarioId);
    }

    @Override
    public ContratoDomain buscarPorId(Long id, Long usuarioId) {
        return repositoryPort.buscarPorId(id, usuarioId);
    }

    @Override
    public List<ContratoDomain> buscarPorImovelId(Long id, Long usuarioId) {
        return repositoryPort.buscarPorImovelId(id, usuarioId);
    }
}
