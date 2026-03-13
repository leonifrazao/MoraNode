package com.leonifrazao.MoraNode.domain.port.out;

import com.leonifrazao.MoraNode.domain.model.ImovelDomain;

import java.util.List;

public interface ImovelRepositoryPort {
    void salvar(ImovelDomain imovel);
    ImovelDomain buscarPorID(Long id, Long usuarioId);
    List<ImovelDomain> buscar(Long usuarioId);
    void editarPorID(Long id, ImovelDomain imovelAtualizado, Long usuarioId);
    void deletar(Long id, Long usuarioId);
}
