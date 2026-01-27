package com.br.financialmanager.application.gateways.transaction;

import com.br.financialmanager.domain.transaction.Transacao;

public interface PublicadorDeTransacao {
  void publicarSolicitacao(Transacao transacao);
}
