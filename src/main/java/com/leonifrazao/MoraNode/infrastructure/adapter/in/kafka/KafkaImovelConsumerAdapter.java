package com.leonifrazao.MoraNode.infrastructure.adapter.in.kafka;

import com.leonifrazao.MoraNode.domain.model.events.ImovelDesocupadoEvent;
import com.leonifrazao.MoraNode.domain.model.events.ImovelOcupadoEvent;
import com.leonifrazao.MoraNode.domain.port.in.CadastrarImovelUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaImovelConsumerAdapter {

    private final CadastrarImovelUseCase cadastrarImovelUseCase;

    @KafkaListener(topics = "imovel-ocupado-topic", groupId = "moranode-group")
    public void ouvirImovelOcupado(ImovelOcupadoEvent evento) {
        log.info("Kafka: Recebida ocupação do imóvel ID {}", evento.imovelId());
        cadastrarImovelUseCase.alterarDisponibilidade(evento.imovelId(), false, evento.usuarioId());
    }

    @KafkaListener(topics = "imovel-desocupado-topic", groupId = "moranode-group")
    public void ouvirImovelDescupado(ImovelDesocupadoEvent evento) {
        log.info("Kafka: Recebida desocupação do imóvel ID {}", evento.imovelId());
        cadastrarImovelUseCase.alterarDisponibilidade(evento.imovelId(), true, evento.usuarioId());
    }
}
