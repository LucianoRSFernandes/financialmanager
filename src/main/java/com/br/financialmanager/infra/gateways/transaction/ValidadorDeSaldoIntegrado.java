package com.br.financialmanager.infra.gateways.transaction;

import com.br.financialmanager.application.gateways.transaction.ValidadorDeSaldo;
import com.br.financialmanager.infra.gateways.http.BrasilApiClient;
import com.br.financialmanager.infra.gateways.http.ContaMockDto;
import com.br.financialmanager.infra.gateways.http.MockApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ValidadorDeSaldoIntegrado implements ValidadorDeSaldo {

  private static final Logger log = LoggerFactory.getLogger(ValidadorDeSaldoIntegrado.class);

  private final MockApiClient mockApiClient;
  private final BrasilApiClient brasilApiClient;

  public ValidadorDeSaldoIntegrado(MockApiClient mockApiClient, BrasilApiClient brasilApiClient) {
    this.mockApiClient = mockApiClient;
    this.brasilApiClient = brasilApiClient;
  }

  @Override
  public boolean saldoEhSuficiente(String cpf, BigDecimal valorTransacao) {
    try {
      log.info("====== INICIANDO VALIDAÇÕES EXTERNAS ======");

      BigDecimal cotacao = buscarCotacaoDolarRecente();
      if (cotacao != null) {
        log.info("💵 [BrasilAPI] Dólar: R$ {}", cotacao);
      } else {
        log.warn("⚠️ [BrasilAPI] Sem cotação recente disponível.");
      }

      String cpfLimpo = cpf.replaceAll("\\D", "");

      List<ContaMockDto> contasEncontradas = mockApiClient.buscarPorCpf(cpfLimpo);

      if (contasEncontradas.isEmpty()) {
        log.warn("⚠️ [MockAPI] Nenhuma conta encontrada para o CPF: {}", cpfLimpo);
        return false;
      }

      ContaMockDto conta = contasEncontradas.get(0);

      log.info("💰 [MockAPI] Conta encontrada ID: {} | Saldo: {} | Limite: {}",
        conta.id(), conta.saldo(), conta.limite());

      BigDecimal totalDisponivel = conta.saldo().add(conta.limite());
      boolean aprovado = totalDisponivel.compareTo(valorTransacao) >= 0;

      log.info("🏁 Resultado: {}", (aprovado ? "APROVADO" : "REJEITADO"));
      log.info("===========================================");

      return aprovado;

    } catch (Exception e) {
      log.error("❌ Erro na integração: {}", e.getMessage(), e);
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
        log.debug("Tentativa de cotação falhou para data {}: {}", data, e.getMessage());
      }
      data = data.minusDays(1);
    }
    return null;
  }
}
