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
    public List<ContratoDomain> buscar() {
        return repositoryPort.buscar();
    }

    @Override
    public ContratoDomain buscarPorId(Long id) {
        return repositoryPort.buscarPorId(id);
    }

    @Override
    public List<ContratoDomain> buscarPorImovelId(Long id) {
        return repositoryPort.buscarPorImovelId(id);
    }
}
