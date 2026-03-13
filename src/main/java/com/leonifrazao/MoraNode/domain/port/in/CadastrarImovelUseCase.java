package com.leonifrazao.MoraNode.domain.port.in;

import com.leonifrazao.MoraNode.domain.model.ImovelDomain;

public interface CadastrarImovelUseCase {
    void cadastrar(ImovelDomain dominio, Long usuarioId);
    void editarPorID(Long id, ImovelDomain dominio, Long usuarioId);
    void alterarDisponibilidade(Long id, boolean disponivel, Long usuarioId);
}
