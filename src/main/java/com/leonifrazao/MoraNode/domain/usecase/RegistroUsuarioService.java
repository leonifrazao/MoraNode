package com.leonifrazao.MoraNode.domain.usecase;

import com.leonifrazao.MoraNode.domain.port.in.RegistroUsuarioUseCase;
import com.leonifrazao.MoraNode.domain.port.out.UsuarioRepositoryPort;

public class RegistroUsuarioService implements RegistroUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public RegistroUsuarioService(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public void registrar(String nome, String email, String senhaHash) {
        if (usuarioRepositoryPort.existePorEmail(email)) {
            throw new IllegalArgumentException("Email já cadastrado!");
        }

        usuarioRepositoryPort.salvar(nome, email, senhaHash, "USUARIO");
    }
}
