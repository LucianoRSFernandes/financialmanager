package com.br.financialmanager.infra.gateways.http;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange("/contas")
public interface MockApiClient {

  @GetExchange
  List<ContaMockDto> buscarPorCpf(@RequestParam("cpf") String cpf);
}
