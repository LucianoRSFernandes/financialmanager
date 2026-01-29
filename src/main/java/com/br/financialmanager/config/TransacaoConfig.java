package com.br.financialmanager.config;

import com.br.financialmanager.application.gateways.transaction.PublicadorDeTransacao;
import com.br.financialmanager.application.gateways.transaction.RepositorioDeTransacao;
import com.br.financialmanager.application.gateways.transaction.ServicoDeCotacao;
import com.br.financialmanager.application.gateways.transaction.ValidadorDeSaldo;
import com.br.financialmanager.application.usecases.transaction.*;
import com.br.financialmanager.infra.gateways.transaction.RepositorioDeTransacaoJpa;
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
  ListarTransacoes listarTransacoes(RepositorioDeTransacao repo) {
    return new ListarTransacoes(repo);
  }

  @Bean
  BuscarTransacao buscarTransacao(RepositorioDeTransacao repo) {
    return new BuscarTransacao(repo);
  }

  @Bean
  ProcessarTransacao processarTransacao(RepositorioDeTransacao repo,
                                        ValidadorDeSaldo validador,
                                        ServicoDeCotacao cotacao) {
    return new ProcessarTransacao(repo, validador, cotacao);
  }

  @Bean
  RepositorioDeTransacaoJpa repositorioDeTransacaoJpa(TransacaoRepository repository) {
    return new RepositorioDeTransacaoJpa(repository);
  }

  @Bean
  public NewTopic topicTransactionRequested() {
    return TopicBuilder.name("transaction.requested").partitions(1).replicas(1).build();
  }

  @Bean
  public NewTopic topicTransactionRequestedDlt() {
    return TopicBuilder.name("transaction.requested.DLT").partitions(1).replicas(1).build();
  }

  @Bean
  GerarAnaliseFinanceira gerarAnaliseFinanceira(RepositorioDeTransacao repo) {
    return new GerarAnaliseFinanceira(repo);
  }

  @Bean
  CancelarTransacao cancelarTransacao(RepositorioDeTransacao repo) {
    return new CancelarTransacao(repo);
  }
}
