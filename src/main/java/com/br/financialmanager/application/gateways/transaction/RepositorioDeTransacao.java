package com.br.financialmanager.application.gateways.transaction;

import com.br.financialmanager.domain.transaction.Transacao;

import java.util.List;

public interface RepositorioDeTransacao {
  Transacao salvar(Transacao transacao);
  List<Transacao> listarTodas();
  Transacao buscarPorId(String id);
}
