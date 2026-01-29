package com.br.financialmanager.application.usecases.transaction;

import com.br.financialmanager.application.gateways.transaction.RepositorioDeTransacao;
import com.br.financialmanager.application.gateways.transaction.ServicoDeCotacao;
import com.br.financialmanager.application.gateways.transaction.ValidadorDeSaldo;
import com.br.financialmanager.domain.transaction.CategoriaTransacao;
import com.br.financialmanager.domain.transaction.StatusTransacao;
import com.br.financialmanager.domain.transaction.TipoTransacao;
import com.br.financialmanager.domain.transaction.Transacao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProcessarTransacao {

  private static final Logger log = LoggerFactory.getLogger(ProcessarTransacao.class);
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
        log.info("💱 Conversão: {} {} -> R$ {}", valorOriginal, moedaFinal, valorFinalBrl);
      }

      boolean aprovado = true;
      if (tipo == TipoTransacao.SAIDA || tipo == TipoTransacao.TRANSFERENCIA) {
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
        LocalDateTime.now(),
        null
      );

      repositorio.salvar(transacaoAtualizada);
      log.info("✅ Transação processada: {}", statusFinal);

    } catch (Exception e) {
      log.error("Erro processando transação: {}", e.getMessage(), e);
    }
  }
}
