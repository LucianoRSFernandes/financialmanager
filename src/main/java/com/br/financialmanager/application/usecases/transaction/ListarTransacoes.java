package com.br.financialmanager.application.usecases.transaction;

import com.br.financialmanager.application.gateways.transaction.FiltroTransacao;
import com.br.financialmanager.application.gateways.transaction.RepositorioDeTransacao;
import com.br.financialmanager.domain.transaction.Pagina;
import com.br.financialmanager.domain.transaction.Transacao;

public class ListarTransacoes {

  private final RepositorioDeTransacao repositorio;

  public ListarTransacoes(RepositorioDeTransacao repositorio) {
    this.repositorio = repositorio;
  }

  public Pagina<Transacao> executar(FiltroTransacao filtro, int pagina, int tamanho) {
    return this.repositorio.listar(filtro, pagina, tamanho);
  }

  public java.util.List<Transacao> obterTodasTransacoes() {
    return this.repositorio.listarTodas();
  }
}
