package com.leonifrazao.MoraNode.domain.port.out;

import com.leonifrazao.MoraNode.domain.model.events.ImovelOcupadoEvent;

public interface NotificacaoImovelPort {
    void avisarImovelOcupado(ImovelOcupadoEvent evento);
}
