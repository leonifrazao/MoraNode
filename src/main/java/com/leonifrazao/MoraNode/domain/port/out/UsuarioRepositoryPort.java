package com.leonifrazao.MoraNode.domain.port.out;

import java.util.Optional;

public interface UsuarioRepositoryPort {
    void salvar(String nome, String email, String senhaHash, String papel);
    Optional<UsuarioRepositoryPort.UsuarioBuscado> buscarPorEmail(String email);
    boolean existePorEmail(String email);

    record UsuarioBuscado(Long id, String nome, String email, String senhaHash, String papel, boolean ativo) {}
}
