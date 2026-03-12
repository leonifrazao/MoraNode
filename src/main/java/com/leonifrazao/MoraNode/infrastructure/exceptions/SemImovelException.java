package com.leonifrazao.MoraNode.infrastructure.exceptions;

public class SemImovelException extends RuntimeException{
    // Construtor padrão
    public SemImovelException() {
        super("Sem casa para realizar a edição.");
    }

    // Construtor que aceita uma mensagem customizada
    public SemImovelException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
