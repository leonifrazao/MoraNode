package com.leonifrazao.MoraNode.infrastructure.adapter.out;

import com.leonifrazao.MoraNode.domain.model.events.ImovelDesocupadoEvent;
import com.leonifrazao.MoraNode.domain.model.events.ImovelOcupadoEvent;
import com.leonifrazao.MoraNode.domain.port.out.NotificacaoImovelPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaImovelProducerAdapter implements NotificacaoImovelPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void avisarImovelOcupado(ImovelOcupadoEvent evento) {
        log.info("Enviando evento de ocupação para o Kafka: Imovel {}", evento.imovelId());

        kafkaTemplate.send("imovel-ocupado-topic", evento.imovelId().toString(), evento);
    }

    @Override
    public void avisarImovelDesocupado(ImovelDesocupadoEvent evento) {
        log.info("Enviando evento de desocupação para o Kafka: Imovel {}", evento.imovelId());

        kafkaTemplate.send("imovel-desocupado-topic", evento.imovelId().toString(), evento);
    }
}
