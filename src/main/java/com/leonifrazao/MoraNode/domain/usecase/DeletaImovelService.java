package com.leonifrazao.MoraNode.domain.usecase;

import com.leonifrazao.MoraNode.domain.port.in.DeletaImovelUseCase;
import com.leonifrazao.MoraNode.domain.port.out.ContratoRepositoryPort;
import com.leonifrazao.MoraNode.domain.port.out.ImovelRepositoryPort;
import com.leonifrazao.MoraNode.infrastructure.exceptions.ImovelComContratoAtivo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeletaImovelService implements DeletaImovelUseCase {

    private final ImovelRepositoryPort repositoryPort;
    private final ContratoRepositoryPort contratoRepositoryPort;


    @Override
    public void deletar(Long id, Long usuarioId) {

        repositoryPort.buscarPorID(id, usuarioId); // Garante que o imóvel é do usuário

        if (contratoRepositoryPort.existeContratoAtivoParaImovel(id, usuarioId)) {
            throw new ImovelComContratoAtivo();
        }

        repositoryPort.deletar(id, usuarioId);
    }
}
