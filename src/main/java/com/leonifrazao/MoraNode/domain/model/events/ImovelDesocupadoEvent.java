package com.leonifrazao.MoraNode.domain.model.events;

import java.io.Serializable;

public record ImovelDesocupadoEvent(
        Long imovelId,
        Long contratoId,
        String tipoContrato,
        Long usuarioId
) implements Serializable {
}

