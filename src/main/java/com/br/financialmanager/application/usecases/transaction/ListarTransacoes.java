package com.br.financialmanager.application.usecases.transaction;

import com.br.financialmanager.application.gateways.transaction.RepositorioDeTransacao;
import com.br.financialmanager.domain.transaction.Transacao;

import java.util.List;

public class ListarTransacoes {

  private final RepositorioDeTransacao repositorio;

  public ListarTransacoes(RepositorioDeTransacao repositorio) {
    this.repositorio = repositorio;
  }

  public List<Transacao> obterTodasTransacoes() {
    return this.repositorio.listarTodas();
  }
}
