package com.leonifrazao.MoraNode.domain.port.in;

import com.leonifrazao.MoraNode.domain.model.ImovelDomain;

public interface CadastrarImovelUseCase {
    void cadastrar(ImovelDomain dominio);
    void editarPorID(Long id, ImovelDomain dominio);
    void alterarDisponibilidade(Long id, boolean disponivel);
}
