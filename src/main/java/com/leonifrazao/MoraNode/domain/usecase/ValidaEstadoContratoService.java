package com.leonifrazao.MoraNode.domain.usecase;

import com.leonifrazao.MoraNode.domain.port.in.ValidaEstadoContratoUseCase;
import com.leonifrazao.MoraNode.domain.port.out.ContratoRepositoryPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValidaEstadoContratoService implements ValidaEstadoContratoUseCase {

    private final ContratoRepositoryPort repositoryPort;

    @Override
    public boolean existeContratoAtivoParaImovel(Long imovelId) {
        return repositoryPort.existeContratoAtivoParaImovel(imovelId);
    }
}
