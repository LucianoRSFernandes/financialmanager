package com.br.financialmanager.infra.gateways.kafka;

import com.br.financialmanager.application.gateways.transaction.PublicadorDeTransacao;
import com.br.financialmanager.domain.transaction.Transacao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerImpl implements PublicadorDeTransacao {

  private static final Logger log = LoggerFactory.getLogger(KafkaProducerImpl.class);
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
      t.getTipo().name(),
      t.isApenasRegistro()
    );

    kafkaTemplate.send("transaction.requested", t.getId(), event);
    log.info("✅ [KAFKA PRODUCER] Evento JSON enviado: {}", event);
  }
}
