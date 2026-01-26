package com.br.financialmanager.domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResumoDiario(
  LocalDate data,
  BigDecimal totalSaida,
  BigDecimal totalEntrada,
  BigDecimal saldoDoDia
) {}
