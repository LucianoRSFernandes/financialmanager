package com.br.financialmanager.infra.gateways.kafka;

import com.br.financialmanager.application.usecases.ProcessarTransacao;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class KafkaConsumerImpl {

  private final ProcessarTransacao processarTransacao;

  public KafkaConsumerImpl(ProcessarTransacao processarTransacao) {
    this.processarTransacao = processarTransacao;
  }

  @KafkaListener(topics = "transaction.requested", groupId = "financeiro-group")
  public void consumirSolicitacao(TransacaoEvent evento) {
    try {
      System.out.println("📥 [KAFKA CONSUMER] Recebido: " + evento);

      processarTransacao.executar(
        evento.id(),
        evento.usuarioId(),
        evento.valor(),
        evento.moeda(),
        evento.tipo()
      );

    } catch (Exception e) {
      // Em um cenário real com DLQ configurada, lançaríamos a exceção aqui
      // para o Kafka tentar novamente ou mover para DLQ.
      System.err.println("❌ Erro fatal ao processar mensagem: " + e.getMessage());
      // throw e; // Descomentar quando configurarmos a DLQ
    }
  }
}
