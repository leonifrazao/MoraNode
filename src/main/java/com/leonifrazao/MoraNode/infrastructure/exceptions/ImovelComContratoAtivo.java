package com.leonifrazao.MoraNode.infrastructure.exceptions;

public class ImovelComContratoAtivo extends RuntimeException {
    public ImovelComContratoAtivo() { super("Imovel ainda possui contratos ativos!");}

    public ImovelComContratoAtivo(String message) {
        super(message);
    }
}
