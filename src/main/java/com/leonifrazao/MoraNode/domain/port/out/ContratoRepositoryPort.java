package com.leonifrazao.MoraNode.domain.port.out;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;
import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;

import java.util.List;

public interface ContratoRepositoryPort {
    List<ContratoDomain> buscar(Long usuarioId);
    void salvar(ContratoDomain dominio);
    ContratoDomain buscarPorId(Long id, Long usuarioId);
    List<ContratoDomain> buscarPorImovelId(Long imovelId, Long usuarioId);
    boolean existeContratoAtivoParaImovel(Long imovelId, Long usuarioId);
    void atualizarStatus(Long id, StatusContrato novoStatus, Long usuarioId);
}
