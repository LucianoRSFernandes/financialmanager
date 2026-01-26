package com.br.financialmanager.application.usecases;


import com.br.financialmanager.application.gateways.RepositorioDeTransacao;
import com.br.financialmanager.application.gateways.ServicoDeCotacao;
import com.br.financialmanager.application.gateways.ValidadorDeSaldo;
import com.br.financialmanager.domain.transaction.StatusTransacao;
import com.br.financialmanager.domain.transaction.TipoTransacao;
import com.br.financialmanager.domain.transaction.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProcessarTransacao {

  private final RepositorioDeTransacao repositorio;
  private final ValidadorDeSaldo validadorSaldo;
  private final ServicoDeCotacao servicoDeCotacao;

  public ProcessarTransacao(RepositorioDeTransacao repositorio,
                            ValidadorDeSaldo validadorSaldo,
                            ServicoDeCotacao servicoDeCotacao) {
    this.repositorio = repositorio;
    this.validadorSaldo = validadorSaldo;
    this.servicoDeCotacao = servicoDeCotacao;
  }

  public void executar(String transacaoId, String cpf, BigDecimal valorOriginal, String moeda, String tipoString) {

    TipoTransacao tipo = TipoTransacao.valueOf(tipoString);
    String moedaFinal = moeda != null ? moeda : "BRL";

    BigDecimal taxa = BigDecimal.ONE;
    BigDecimal valorFinalBrl = valorOriginal;

    try {
      if (!"BRL".equalsIgnoreCase(moedaFinal)) {
        taxa = servicoDeCotacao.obterCotacao(moedaFinal);
        valorFinalBrl = valorOriginal.multiply(taxa);
        System.out.println("💱 Conversão: " + valorOriginal + " " + moedaFinal + " -> R$ " + valorFinalBrl);
      }

      boolean aprovado = true;
      if (tipo == TipoTransacao.SAIDA) {
        aprovado = validadorSaldo.saldoEhSuficiente(cpf, valorFinalBrl);
      }

      StatusTransacao statusFinal = aprovado ? StatusTransacao.APROVADA : StatusTransacao.REJEITADA;

      Transacao transacaoAtualizada = new Transacao(
        transacaoId,
        cpf,
        valorOriginal,
        moedaFinal,
        tipo,
        valorFinalBrl,
        taxa,
        statusFinal,
        LocalDateTime.now() // Data atual para aparecer no relatório
      );

      repositorio.salvar(transacaoAtualizada);
      System.out.println("✅ Transação processada: " + statusFinal);

    } catch (Exception e) {
      System.err.println("Erro processando transação: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
