package com.br.financialmanager.infra.gateways.transaction;

import com.br.financialmanager.domain.transaction.Transacao;
import com.br.financialmanager.infra.persistence.TransacaoEntity;

public class TransacaoMapper {

  public static TransacaoEntity toEntity(Transacao t) {
    return new TransacaoEntity(
      t.getId(),
      t.getUsuarioId(),
      t.getValorOriginal(),
      t.getMoeda(),
      t.getTipo(),
      t.getValorBrl(),
      t.getTaxaConversao(),
      t.getStatus(),
      t.getDataCriacao(),
      t.getCategoria()
    );
  }

  public static Transacao toDomain(TransacaoEntity e) {
    return new Transacao(
      e.getId(),
      e.getUsuarioId(),
      e.getValorOriginal(),
      e.getMoeda(),
      e.getTipo(),
      e.getValorBrl(),
      e.getTaxaConversao(),
      e.getStatus(),
      e.getDataCriacao(),
      e.getCategoria()
    );
  }
}
