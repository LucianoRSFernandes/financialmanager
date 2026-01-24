package com.br.financialmanager.infra.gateways;

import com.br.financialmanager.domain.transaction.Transacao;
import com.br.financialmanager.infra.persistence.TransacaoEntity;

public class TransacaoMapper {
  public static TransacaoEntity toEntity(Transacao t) {
    return new TransacaoEntity(t.getId(), t.getUsuarioId(), t.getValor(), t.getStatus(), t.getDataCriacao());
  }

  public static Transacao toDomain(TransacaoEntity e) {
    return new Transacao(e.getId(), e.getUsuarioId(), e.getValor(), e.getStatus(), e.getDataCriacao());
  }
}
