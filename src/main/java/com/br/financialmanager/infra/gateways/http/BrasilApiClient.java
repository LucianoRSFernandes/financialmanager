package com.br.financialmanager.infra.gateways.http;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/cambio/v1/cotacao")
public interface BrasilApiClient {

  @GetExchange("/{moeda}/{data}")
  BrasilApiCambioDto buscarCotacao(
    @org.springframework.web.bind.annotation
      .PathVariable("moeda") String moeda,
    @org.springframework.web.bind.annotation.
      PathVariable("data") String data);
}
