package com.leonifrazao.MoraNode.domain.port.in;

public interface RegistroUsuarioUseCase {
    void registrar(String nome, String email, String senhaHash);
}
