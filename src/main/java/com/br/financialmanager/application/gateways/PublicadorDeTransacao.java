package com.br.financialmanager.application.gateways;

import com.br.financialmanager.domain.transaction.Transacao;

public interface PublicadorDeTransacao {
  void publicarSolicitacao(Transacao transacao);
}
