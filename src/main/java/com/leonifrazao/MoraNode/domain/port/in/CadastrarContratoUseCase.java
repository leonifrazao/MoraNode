package com.leonifrazao.MoraNode.domain.port.in;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;
import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;

public interface CadastrarContratoUseCase {
    void cadastrar(ContratoDomain dominio);
    void atualizarStatus(Long id, StatusContrato status);
}
