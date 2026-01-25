package com.br.financialmanager.application.gateways;

import java.math.BigDecimal;

public interface ValidadorDeSaldo {
  boolean saldoEhSuficiente(String cpf, BigDecimal valorTransacao);
}
