package com.br.financialmanager.infra.gateways.http;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/contas")
public interface MockApiClient {

  @GetExchange("/{id}")
  ContaMockDto buscarConta(
    @org.springframework.web.bind.annotation
      .PathVariable("id") String id);
}
