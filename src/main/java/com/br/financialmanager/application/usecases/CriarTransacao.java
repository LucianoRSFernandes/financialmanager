package com.br.financialmanager.application.usecases;

import com.br.financialmanager.application.gateways.PublicadorDeTransacao;
import com.br.financialmanager.application.gateways.RepositorioDeTransacao;
import com.br.financialmanager.domain.transaction.Transacao;

import java.math.BigDecimal;

public class CriarTransacao {

  private final RepositorioDeTransacao repositorio;
  private final PublicadorDeTransacao publicador;

  public CriarTransacao(RepositorioDeTransacao repositorio, PublicadorDeTransacao publicador) {
    this.repositorio = repositorio;
    this.publicador = publicador;
  }

  public Transacao executar(String usuarioCpf, BigDecimal valor) {
    Transacao novaTransacao = new Transacao(usuarioCpf, valor);
    Transacao salva = repositorio.salvar(novaTransacao);
    publicador.publicarSolicitacao(salva);
    return salva;
  }
}
