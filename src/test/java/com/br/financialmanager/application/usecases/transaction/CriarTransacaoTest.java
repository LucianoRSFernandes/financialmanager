package com.br.financialmanager.application.usecases.transaction;

import com.br.financialmanager.application.gateways.transaction.PublicadorDeTransacao;
import com.br.financialmanager.application.gateways.transaction.RepositorioDeTransacao;
import com.br.financialmanager.domain.transaction.CategoriaTransacao;
import com.br.financialmanager.domain.transaction.TipoTransacao;
import com.br.financialmanager.domain.transaction.Transacao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CriarTransacaoTest {

  @Test
  public void deveSalvarEPublicarTransacao() {
    RepositorioDeTransacao repositorio = Mockito.mock(RepositorioDeTransacao.class);
    PublicadorDeTransacao publicador = Mockito.mock(PublicadorDeTransacao.class);

    CriarTransacao useCase = new CriarTransacao(repositorio, publicador);

    when(repositorio.salvar(any(Transacao.class)))
      .thenAnswer(invocation -> invocation.getArgument(0));

    Transacao resultado = useCase.executar(
      "123.456.789-00",
      new BigDecimal("100"),
      "BRL",
      TipoTransacao.SAIDA,
      CategoriaTransacao.COMPRA,
      false
    );

    Assertions.assertNotNull(resultado);
    verify(repositorio).salvar(any(Transacao.class));
    verify(publicador).publicarSolicitacao(any(Transacao.class));
  }
}
