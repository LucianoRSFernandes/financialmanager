package com.br.financialmanager.application.gateways.transaction;

import java.math.BigDecimal;

public interface ValidadorDeSaldo {
  boolean saldoEhSuficiente(String cpf, BigDecimal valorTransacao);
}
