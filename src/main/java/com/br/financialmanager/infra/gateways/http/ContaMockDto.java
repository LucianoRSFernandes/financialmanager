package com.br.financialmanager.infra.gateways.http;

import java.math.BigDecimal;

public record ContaMockDto(
  String id,
  BigDecimal saldo,
  BigDecimal limite
) {}
