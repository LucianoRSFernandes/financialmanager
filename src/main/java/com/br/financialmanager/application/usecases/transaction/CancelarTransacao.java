package com.br.financialmanager.application.usecases.transaction;

import com.br.financialmanager.application.gateways.transaction.RepositorioDeTransacao;
import com.br.financialmanager.domain.transaction.Transacao;

public class CancelarTransacao {

  private final RepositorioDeTransacao repositorio;

  public CancelarTransacao(RepositorioDeTransacao repositorio) {
    this.repositorio = repositorio;
  }

  public void executar(String id) {
    Transacao transacao = repositorio.buscarPorId(id);
    if (transacao == null) {
      throw new IllegalArgumentException("Transação não encontrada");
    }
    transacao.cancelar();
    repositorio.salvar(transacao);
  }
}
