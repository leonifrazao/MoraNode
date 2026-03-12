package com.leonifrazao.MoraNode.domain.port.out;

import com.leonifrazao.MoraNode.domain.model.ImovelDomain;

import java.util.List;

public interface ImovelRepositoryPort {
    void salvar(ImovelDomain imovel);
    ImovelDomain buscarPorID(Long id);
    List<ImovelDomain> buscar();
    void editarPorID(Long id, ImovelDomain imovelAtualizado);
    void deletar(Long id);
}
