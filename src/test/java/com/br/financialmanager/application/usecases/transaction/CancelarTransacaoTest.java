package com.br.financialmanager.application.usecases.transaction;

import com.br.financialmanager.application.gateways.transaction.RepositorioDeTransacao;
import com.br.financialmanager.domain.transaction.CategoriaTransacao;
import com.br.financialmanager.domain.transaction.StatusTransacao;
import com.br.financialmanager.domain.transaction.TipoTransacao;
import com.br.financialmanager.domain.transaction.Transacao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CancelarTransacaoTest {

  @Test
  public void deveCancelarComSucesso() {
    RepositorioDeTransacao repositorio = Mockito.mock(RepositorioDeTransacao.class);
    CancelarTransacao useCase = new CancelarTransacao(repositorio);

    Transacao transacaoPendente = new Transacao(
      "user-1",
      BigDecimal.TEN,
      "BRL",
      TipoTransacao.SAIDA,
      CategoriaTransacao.OUTROS,
      false
    );
    String idTransacao = transacaoPendente.getId();

    when(repositorio.buscarPorId(idTransacao)).thenReturn(transacaoPendente);

    useCase.executar(idTransacao);

    Assertions.assertEquals(StatusTransacao.CANCELADA, transacaoPendente.getStatus());
    verify(repositorio).salvar(transacaoPendente);
  }

  @Test
  public void deveLancarErroSeTransacaoNaoExiste() {
    RepositorioDeTransacao repositorio = Mockito.mock(RepositorioDeTransacao.class);
    CancelarTransacao useCase = new CancelarTransacao(repositorio);

    when(repositorio.buscarPorId("id-inexistente")).thenReturn(null);

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      useCase.executar("id-inexistente");
    });

    verify(repositorio, never()).salvar(any());
  }
}
