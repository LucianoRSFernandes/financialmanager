package com.br.financialmanager.infra.gateways.kafka;

import java.math.BigDecimal;

public record TransacaoEvent(
  String id,
  String usuarioId,
  BigDecimal valor,
  String moeda,
  String tipo
) {}
