package com.br.financialmanager.infra.gateways.http;

import java.math.BigDecimal;

public record CotacaoDetalheDto(
  BigDecimal cotacao_venda,
  BigDecimal cotacao_compra,
  String data_hora_cotacao
) {}
