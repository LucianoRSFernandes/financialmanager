package com.br.financialmanager.config;

import com.br.financialmanager.application.gateways.PublicadorDeTransacao;
import com.br.financialmanager.application.gateways.RepositorioDeTransacao;
import com.br.financialmanager.application.usecases.CriarTransacao;
import com.br.financialmanager.infra.gateways.RepositorioDeTransacaoJpa;
import com.br.financialmanager.infra.persistence.TransacaoRepository;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TransacaoConfig {

  @Bean
  CriarTransacao criarTransacao(RepositorioDeTransacao repo, PublicadorDeTransacao pub) {
    return new CriarTransacao(repo, pub);
  }

  @Bean
  RepositorioDeTransacaoJpa repositorioDeTransacaoJpa(TransacaoRepository repository) {
    return new RepositorioDeTransacaoJpa(repository);
  }

   @Bean
  public NewTopic topicTransactionRequested() {
    return TopicBuilder.name("transaction.requested").partitions(1).replicas(1).build();
  }
}
