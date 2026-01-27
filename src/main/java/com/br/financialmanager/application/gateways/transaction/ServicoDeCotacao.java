package com.br.financialmanager.application.gateways.transaction;

import java.math.BigDecimal;

public interface ServicoDeCotacao {
  BigDecimal obterCotacao(String moedaOrigem);
}
