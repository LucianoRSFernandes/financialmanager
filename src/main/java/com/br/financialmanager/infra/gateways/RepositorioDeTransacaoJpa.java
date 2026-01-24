package com.br.financialmanager.infra.gateways;

import com.br.financialmanager.application.gateways.RepositorioDeTransacao;
import com.br.financialmanager.domain.transaction.Transacao;
import com.br.financialmanager.infra.persistence.TransacaoRepository;

public class RepositorioDeTransacaoJpa implements RepositorioDeTransacao {

  private final TransacaoRepository repository;

  public RepositorioDeTransacaoJpa(TransacaoRepository repository) {
    this.repository = repository;
  }

  @Override
  public Transacao salvar(Transacao transacao) {
    var entity = TransacaoMapper.toEntity(transacao);
    repository.save(entity);
    return TransacaoMapper.toDomain(entity);
  }
}
