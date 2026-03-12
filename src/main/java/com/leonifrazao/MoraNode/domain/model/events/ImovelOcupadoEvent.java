package com.leonifrazao.MoraNode.domain.model.events;

import java.io.Serializable;

public record ImovelOcupadoEvent(
        Long imovelId,
        Long contratoId,
        String tipoContrato
) implements Serializable {
}
