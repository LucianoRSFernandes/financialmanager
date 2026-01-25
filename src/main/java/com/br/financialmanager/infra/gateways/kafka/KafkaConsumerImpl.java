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
  public void consumirSolicitacao(String mensagem) {
    try {
      System.out.println("<<< KAFKA: Recebendo mensagem: " + mensagem);

      String[] partes = mensagem.split(";");
      String id = partes[0];
      String cpf = partes[1];
      BigDecimal valor = new BigDecimal(partes[2]);

      processarTransacao.executar(id, cpf, valor);

    } catch (Exception e) {
      System.err.println("Erro ao ler mensagem do Kafka: " + e.getMessage());
    }
  }
}
