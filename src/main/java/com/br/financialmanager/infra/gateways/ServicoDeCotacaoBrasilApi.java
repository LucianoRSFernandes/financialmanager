package com.br.financialmanager.infra.gateways;

import com.br.financialmanager.application.gateways.transaction.ServicoDeCotacao;
import com.br.financialmanager.infra.gateways.http.BrasilApiClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ServicoDeCotacaoBrasilApi implements ServicoDeCotacao {

  private final BrasilApiClient client;

  public ServicoDeCotacaoBrasilApi(BrasilApiClient client) {
    this.client = client;
  }

  @Override
  public BigDecimal obterCotacao(String moedaOrigem) {
    if ("BRL".equalsIgnoreCase(moedaOrigem)) {
      return BigDecimal.ONE;
    }

    try {
      LocalDate data = LocalDate.now();
      DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

      for (int i = 0; i < 5; i++) {
        try {
          String dataFormatada = data.minusDays(i).format(fmt);
          String dataBrasilApi = data.minusDays(i).format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));

          var response = client.buscarCotacao(moedaOrigem, dataBrasilApi);

          if (response != null && !response.cotacoes().isEmpty()) {
            return response.cotacoes().get(response.cotacoes().size() - 1).cotacao_venda();
          }
        } catch (Exception ignored) { }
      }
    } catch (Exception e) {
      System.err.println("Erro ao buscar cotação: " + e.getMessage());
    }

    throw new RuntimeException("Não foi possível obter cotação para " + moedaOrigem);
  }
}
