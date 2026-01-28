package com.br.financialmanager.infra.gateways.kafka;

import com.br.financialmanager.application.usecases.transaction.ProcessarTransacao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Profile("worker")
public class KafkaConsumerImpl {

  private static final Logger log = LoggerFactory.getLogger(KafkaConsumerImpl.class);
  private final ProcessarTransacao processarTransacao;

  public KafkaConsumerImpl(ProcessarTransacao processarTransacao) {
    this.processarTransacao = processarTransacao;
  }

  @KafkaListener(topics = "transaction.requested", groupId = "financeiro-group")
  public void consumirSolicitacao(TransacaoEvent evento) {
    log.info("📥 [KAFKA CONSUMER] Recebido: {}", evento);

    processarTransacao.executar(
      evento.id(),
      evento.usuarioId(),
      evento.valor(),
      evento.moeda(),
      evento.tipo()
    );
  }
}
