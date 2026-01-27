package com.br.financialmanager.domain.transaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class TransacaoTest {

  @Test
  public void deveCriarTransacaoPadraoBRL() {
    Transacao transacao = new Transacao(
      "123",
      new BigDecimal("100.00"),
      "BRL",
      TipoTransacao.SAIDA
    );

    Assertions.assertEquals("BRL", transacao.getMoeda());
    Assertions.assertEquals(BigDecimal.ONE, transacao.getTaxaConversao());
    Assertions.assertEquals(StatusTransacao.PENDENTE, transacao.getStatus());
    Assertions.assertNotNull(transacao.getId());
    Assertions.assertNotNull(transacao.getDataCriacao());
  }

  @Test
  public void deveNormalizarMoedaParaMaiusculo() {
    Transacao transacao = new Transacao(
      "123",
      new BigDecimal("50.00"),
      "usd",
      TipoTransacao.ENTRADA
    );

    Assertions.assertEquals("USD", transacao.getMoeda());
  }
}
