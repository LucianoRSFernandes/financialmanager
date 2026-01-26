package com.br.financialmanager.infra.controller;

import java.math.BigDecimal;

public record TransacaoDto(
  String cpf,
  BigDecimal valor,
  String moeda,
  String tipo
) {}
