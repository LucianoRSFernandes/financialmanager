package com.br.financialmanager.application.gateways;

import com.br.financialmanager.domain.transaction.Transacao;

public interface RepositorioDeTransacao {
  Transacao salvar(Transacao transacao);
}
