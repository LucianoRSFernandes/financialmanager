package com.br.financialmanager.application.gateways;

import java.math.BigDecimal;

public interface ServicoDeCotacao {
  BigDecimal obterCotacao(String moedaOrigem);
}
