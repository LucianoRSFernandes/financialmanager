package com.br.financialmanager.infra.gateways.kafka;

import com.br.financialmanager.application.gateways.transaction.PublicadorDeTransacao;
import com.br.financialmanager.domain.transaction.Transacao;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerImpl implements PublicadorDeTransacao {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public KafkaProducerImpl(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void publicarSolicitacao(Transacao t) {
    TransacaoEvent event = new TransacaoEvent(
      t.getId(),
      t.getUsuarioId(),
      t.getValorOriginal(),
      t.getMoeda(),
      t.getTipo().name()
    );

    kafkaTemplate.send("transaction.requested", t.getId(), event);
    System.out.println("✅ [KAFKA PRODUCER] Evento JSON enviado: " + event);
  }
}
