package com.br.financialmanager.domain.transaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import java.math.BigDecimal;

public class TransacaoTest {

  @Test
  public void deveCriarTransacaoPadraoBRL() {
    Transacao transacao = new Transacao(
      "123",
      new BigDecimal("100.00"),
      "BRL",
      TipoTransacao.SAIDA,
      CategoriaTransacao.OUTROS,
      false
    );

    Assertions.assertEquals("BRL", transacao.getMoeda());
    Assertions.assertEquals(BigDecimal.ONE, transacao.getTaxaConversao());
    Assertions.assertEquals(StatusTransacao.PENDENTE, transacao.getStatus());
    Assertions.assertNotNull(transacao.getId());
    Assertions.assertNotNull(transacao.getDataCriacao());
    Assertions.assertFalse(transacao.isApenasRegistro());
  }

  @Test
  public void deveNormalizarMoedaParaMaiusculo() {
    Transacao transacao = new Transacao(
      "123",
      new BigDecimal("50.00"),
      "usd",
      TipoTransacao.ENTRADA,
      CategoriaTransacao.OUTROS,
      false
    );

    Assertions.assertEquals("USD", transacao.getMoeda());
  }

  @Test
  public void deveCancelarTransacaoPendente() {
    Transacao transacao = new Transacao(
      "123",
      BigDecimal.TEN,
      "BRL",
      TipoTransacao.SAIDA,
      CategoriaTransacao.OUTROS,
      false
    );

    Assertions.assertEquals(StatusTransacao.PENDENTE, transacao.getStatus());
    transacao.cancelar();
    Assertions.assertEquals(StatusTransacao.CANCELADA, transacao.getStatus());
  }

  @Test
  public void naoDeveCancelarTransacaoJaAprovada() {
    Transacao transacao = new Transacao(
      "123",
      BigDecimal.TEN,
      "BRL",
      TipoTransacao.SAIDA,
      CategoriaTransacao.OUTROS,
      false
    );

    transacao.definirStatus(StatusTransacao.APROVADA);

    IllegalStateException exception = Assertions.assertThrows(
      IllegalStateException.class,
      transacao::cancelar
    );

    Assertions.assertEquals("Apenas transações pendentes podem ser canceladas.", exception.getMessage());
  }

  @Test
  public void deveAssumirCategoriaOutrosQuandoForNula() {
    Transacao transacao = new Transacao(
      "123",
      new BigDecimal("100.00"),
      "BRL",
      TipoTransacao.SAIDA,
      null,
      false
    );

    Assertions.assertEquals(CategoriaTransacao.OUTROS, transacao.getCategoria());
  }

  @ParameterizedTest
  @EnumSource(CategoriaTransacao.class)
  public void deveAceitarTodasAsCategoriasDefinidas(CategoriaTransacao categoriaTeste) {
    Transacao transacao = new Transacao(
      "123",
      new BigDecimal("100.00"),
      "BRL",
      TipoTransacao.ENTRADA,
      categoriaTeste,
      false
    );

    Assertions.assertEquals(categoriaTeste, transacao.getCategoria());
  }
}
