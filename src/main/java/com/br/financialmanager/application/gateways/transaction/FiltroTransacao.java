package com.br.financialmanager.application.gateways.transaction;

import com.br.financialmanager.domain.transaction.StatusTransacao;
import com.br.financialmanager.domain.transaction.TipoTransacao;
import java.time.LocalDate;

public record FiltroTransacao(
  String usuarioId,
  LocalDate dataInicio,
  LocalDate dataFim,
  StatusTransacao status,
  TipoTransacao tipo
) {}
