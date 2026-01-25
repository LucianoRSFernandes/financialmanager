package com.br.financialmanager.application.usecases;

import com.br.financialmanager.application.gateways.RepositorioDeTransacao;
import com.br.financialmanager.application.gateways.ValidadorDeSaldo;
import com.br.financialmanager.domain.transaction.StatusTransacao;
import com.br.financialmanager.domain.transaction.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProcessarTransacao {

  private final RepositorioDeTransacao repositorio;
  private final ValidadorDeSaldo validadorSaldo;

  public ProcessarTransacao(RepositorioDeTransacao repositorio, ValidadorDeSaldo validadorSaldo) {
    this.repositorio = repositorio;
    this.validadorSaldo = validadorSaldo;
  }

  public void executar(String transacaoId, String cpf, BigDecimal valor) {


    boolean aprovado = validadorSaldo.saldoEhSuficiente(cpf, valor);

    StatusTransacao novoStatus = aprovado ? StatusTransacao.APROVADA : StatusTransacao.REJEITADA;


    Transacao transacao = new Transacao(transacaoId, cpf, valor, novoStatus, LocalDateTime.now());
    repositorio.salvar(transacao);

    System.out.println(">>> USE CASE: Transação " + transacaoId + " processada. Status: " + novoStatus);
  }
}
