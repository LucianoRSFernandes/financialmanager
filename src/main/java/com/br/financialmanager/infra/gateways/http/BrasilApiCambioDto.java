package com.br.financialmanager.infra.gateways.http;

import java.util.List;

public record BrasilApiCambioDto(
  List<CotacaoDetalheDto> cotacoes
) {}

