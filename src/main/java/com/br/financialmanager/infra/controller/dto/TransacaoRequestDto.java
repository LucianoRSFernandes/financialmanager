package com.br.financialmanager.infra.controller.dto;

import com.br.financialmanager.domain.transaction.CategoriaTransacao;
import com.br.financialmanager.domain.transaction.TipoTransacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransacaoRequestDto(
  @NotBlank String cpf,
  @NotNull @DecimalMin("0.01") BigDecimal valor,
  String moeda,
  @NotNull TipoTransacao tipo,
  CategoriaTransacao categoria
) {}
