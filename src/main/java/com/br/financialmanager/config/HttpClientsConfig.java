package com.br.financialmanager.config;

import com.br.financialmanager.infra.gateways.http.BrasilApiClient;
import com.br.financialmanager.infra.gateways.http.MockApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpClientsConfig {

  @Bean
  public MockApiClient mockApiClient(RestClient.Builder builder) {
    // 1. Configura a URL base
    RestClient client = builder
      .baseUrl("https://69752356265838bbea96c99b.mockapi.io/api/v1")
      .build();

    HttpServiceProxyFactory factory = HttpServiceProxyFactory
      .builderFor(RestClientAdapter.create(client))
      .build();
    return factory.createClient(MockApiClient.class);
  }

  @Bean
  public BrasilApiClient brasilApiClient(RestClient.Builder builder) {
    RestClient client = builder
      .baseUrl("https://brasilapi.com.br/api")
      .build();

    HttpServiceProxyFactory factory = HttpServiceProxyFactory
      .builderFor(RestClientAdapter.create(client))
      .build();
    return factory.createClient(BrasilApiClient.class);
  }
}
