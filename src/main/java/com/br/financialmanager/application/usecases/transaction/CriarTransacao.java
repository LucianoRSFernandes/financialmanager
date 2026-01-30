package com.br.financialmanager.application.usecases.transaction;

import com.br.financialmanager.application.gateways.transaction.PublicadorDeTransacao;
import com.br.financialmanager.application.gateways.transaction.RepositorioDeTransacao;
import com.br.financialmanager.domain.transaction.CategoriaTransacao;
import com.br.financialmanager.domain.transaction.TipoTransacao;
import com.br.financialmanager.domain.transaction.Transacao;

import java.math.BigDecimal;

public class CriarTransacao {

  private final RepositorioDeTransacao repositorio;
  private final PublicadorDeTransacao publicador;

  public CriarTransacao(RepositorioDeTransacao repositorio, PublicadorDeTransacao publicador) {
    this.repositorio = repositorio;
    this.publicador = publicador;
  }

  public Transacao executar(String usuarioCpf, BigDecimal valor, String moeda,
                            TipoTransacao tipo, CategoriaTransacao categoria,
                            boolean apenasRegistro) {

    Transacao novaTransacao = new Transacao(usuarioCpf, valor, moeda, tipo, categoria, apenasRegistro);
    Transacao salva = repositorio.salvar(novaTransacao);
    publicador.publicarSolicitacao(salva);

    return salva;
  }
}
