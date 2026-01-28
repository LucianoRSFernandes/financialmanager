package com.br.financialmanager.infra.gateways.transaction;

import com.br.financialmanager.application.gateways.transaction.ValidadorDeSaldo;
import com.br.financialmanager.infra.gateways.http.BrasilApiClient;
import com.br.financialmanager.infra.gateways.http.MockApiClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ValidadorDeSaldoIntegrado implements ValidadorDeSaldo {

  private final MockApiClient mockApiClient;
  private final BrasilApiClient brasilApiClient;

  public ValidadorDeSaldoIntegrado(MockApiClient mockApiClient, BrasilApiClient brasilApiClient) {
    this.mockApiClient = mockApiClient;
    this.brasilApiClient = brasilApiClient;
  }

  @Override
  public boolean saldoEhSuficiente(String cpf, BigDecimal valorTransacao) {
    try {
      System.out.println("====== INICIANDO VALIDAÇÕES EXTERNAS ======");


      BigDecimal cotacao = buscarCotacaoDolarRecente();
      if (cotacao != null) {
        System.out.println("💵 [BrasilAPI] Dólar: R$ " + cotacao);
      } else {
        System.out.println("⚠️ [BrasilAPI] Sem cotação recente disponível.");
      }

      // 2. SALDO (Mock API)
      var conta = mockApiClient.buscarConta("1");
      System.out.println("💰 [MockAPI] Saldo: " + conta.saldo() + " | Limite: " + conta.limite());

      BigDecimal totalDisponivel = conta.saldo().add(conta.limite());
      boolean aprovado = totalDisponivel.compareTo(valorTransacao) >= 0;

      System.out.println("🏁 Resultado: " + (aprovado ? "APROVADO" : "REJEITADO"));
      System.out.println("===========================================");

      return aprovado;

    } catch (Exception e) {
      System.err.println("❌ Erro na integração: " + e.getMessage());
      return false;
    }
  }

  private BigDecimal buscarCotacaoDolarRecente() {
    LocalDate data = LocalDate.now();
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    for (int i = 0; i < 5; i++) {
      try {
        var resp = brasilApiClient.buscarCotacao("USD", data.format(fmt));
        if (resp != null && !resp.cotacoes().isEmpty()) {
          return resp.cotacoes().get(resp.cotacoes().size() - 1).cotacao_venda();
        }
      } catch (Exception e) {

      }
      data = data.minusDays(1);
    }
    return null;
  }
}
