package com.leonifrazao.MoraNode.domain.usecase;

import com.leonifrazao.MoraNode.domain.model.ImovelDomain;
import com.leonifrazao.MoraNode.domain.port.in.DeletaImovelUseCase;
import com.leonifrazao.MoraNode.domain.port.out.ImovelRepositoryPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeletaImovelService implements DeletaImovelUseCase {

    private final ImovelRepositoryPort repositoryPort;


    @Override
    public void deletar(Long id) {
        repositoryPort.deletar(id);
    }
}
