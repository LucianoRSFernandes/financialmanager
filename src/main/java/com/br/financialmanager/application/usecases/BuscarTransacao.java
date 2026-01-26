package com.br.financialmanager.application.usecases;

import com.br.financialmanager.application.gateways.RepositorioDeTransacao;
import com.br.financialmanager.domain.transaction.Transacao;

public class BuscarTransacao {

  private final RepositorioDeTransacao repositorio;

  public BuscarTransacao(RepositorioDeTransacao repositorio) {
    this.repositorio = repositorio;
  }

  public Transacao buscarPorId(String id) {
    return this.repositorio.buscarPorId(id);
  }
}
