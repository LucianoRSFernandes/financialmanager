package com.br.financialmanager.application.gateways.transaction;

import com.br.financialmanager.domain.transaction.Pagina;
import com.br.financialmanager.domain.transaction.Transacao;
import java.util.List;

public interface RepositorioDeTransacao {
  Transacao salvar(Transacao transacao);
  Transacao buscarPorId(String id);

  Pagina<Transacao> listar(FiltroTransacao filtro, int pagina, int tamanho);

  List<Transacao> listarTodas();
}
