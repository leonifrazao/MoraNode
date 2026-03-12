package com.leonifrazao.MoraNode.domain.port.out;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;
import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;

import java.util.List;

public interface ContratoRepositoryPort {
    List<ContratoDomain> buscar();
    void salvar(ContratoDomain dominio);
    ContratoDomain buscarPorId(Long id);
    List<ContratoDomain> buscarPorImovelId(Long imovelId);
    boolean existeContratoAtivoParaImovel(Long imovelId);
    void atualizarStatus(Long id, StatusContrato novoStatus);
}
