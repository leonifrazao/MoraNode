package com.leonifrazao.MoraNode.domain.port.in;

public interface DeletaImovelUseCase {
    void deletar(Long id, Long usuarioId);
}
