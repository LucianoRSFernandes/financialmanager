package com.br.financialmanager.infra.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record TransacaoRequestDto(
  @NotBlank(message = "O CPF é obrigatório")
  @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$", message = "CPF inválido (use 000.000.000-00)")
  String cpf,

  @NotNull(message = "O valor é obrigatório")
  @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
  BigDecimal valor,

  @NotBlank(message = "O tipo (ENTRADA/SAIDA) é obrigatório")
  String tipo,

  String moeda
) {}
