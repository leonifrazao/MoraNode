package com.leonifrazao.MoraNode.domain.port.in;

public interface ValidaEstadoContratoUseCase {
    boolean existeContratoAtivoParaImovel(Long imovelId, Long usuarioId);
}
