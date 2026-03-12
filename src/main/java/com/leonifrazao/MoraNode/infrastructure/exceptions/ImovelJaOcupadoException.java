package com.leonifrazao.MoraNode.infrastructure.exceptions;

public class ImovelJaOcupadoException extends RuntimeException {

    public ImovelJaOcupadoException() {
        super("Imovel já ocupado.");
    }

    public ImovelJaOcupadoException(String message) {
        super(message);
    }
}
