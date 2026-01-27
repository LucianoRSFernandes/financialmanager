package com.br.financialmanager.infra.controller.dto;

import java.math.BigDecimal;

public record TransacaoDto(
  String cpf,
  BigDecimal valor,
  String moeda,
  String tipo
) {}
