package com.br.financialmanager.infra.gateways.kafka;

import com.br.financialmanager.application.gateways.PublicadorDeTransacao;
import com.br.financialmanager.domain.transaction.Transacao;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerImpl implements PublicadorDeTransacao {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public KafkaProducerImpl(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void publicarSolicitacao(Transacao transacao) {
    // Em projetos reais, usaríamos JSON, mas String funciona para o desafio
    String mensagem = transacao.getId() + ";" + transacao.getUsuarioId() + ";" + transacao.getValor();

    kafkaTemplate.send("transaction.requested", mensagem);
    System.out.println("Mensagem enviada para o Kafka: " + mensagem);
  }
}
