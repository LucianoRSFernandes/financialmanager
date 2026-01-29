package com.br.financialmanager.application.usecases.transaction;

import com.br.financialmanager.application.gateways.transaction.RepositorioDeTransacao;
import com.br.financialmanager.application.gateways.transaction.ServicoDeCotacao;
import com.br.financialmanager.application.gateways.transaction.ValidadorDeSaldo;
import com.br.financialmanager.domain.transaction.StatusTransacao;
import com.br.financialmanager.domain.transaction.Transacao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProcessarTransacaoTest {

  @Test
  public void deveAprovarTransacaoComSaldoSuficienteEConverterDolar() {
    RepositorioDeTransacao repositorio = Mockito.mock(RepositorioDeTransacao.class);
    ValidadorDeSaldo validador = Mockito.mock(ValidadorDeSaldo.class);
    ServicoDeCotacao cotacao = Mockito.mock(ServicoDeCotacao.class);

    when(cotacao.obterCotacao("USD")).thenReturn(new BigDecimal("5.00"));
    when(validador.saldoEhSuficiente(any(), any())).thenReturn(true);

    ProcessarTransacao useCase = new ProcessarTransacao(repositorio, validador, cotacao);

    useCase.executar("id-1", "cpf-1", new BigDecimal("100"),
      "USD", "SAIDA");

    ArgumentCaptor<Transacao> captor = ArgumentCaptor.forClass(Transacao.class);
    verify(repositorio).salvar(captor.capture());
    Transacao salva = captor.getValue();

    Assertions.assertEquals(StatusTransacao.APROVADA, salva.getStatus());
    Assertions.assertEquals(new BigDecimal("5.00"), salva.getTaxaConversao());
    Assertions.assertEquals(0, new BigDecimal("500.00")
      .compareTo(salva.getValorBrl()));
  }

  @Test
  public void deveRejeitarSeSaldoInsuficiente() {
    RepositorioDeTransacao repositorio = Mockito.mock(RepositorioDeTransacao.class);
    ValidadorDeSaldo validador = Mockito.mock(ValidadorDeSaldo.class);
    ServicoDeCotacao cotacao = Mockito.mock(ServicoDeCotacao.class);

    when(validador.saldoEhSuficiente(any(), any())).thenReturn(false);

    ProcessarTransacao useCase = new ProcessarTransacao(repositorio, validador, cotacao);

    useCase.executar("id-2", "cpf-1", new BigDecimal("1000"),
      "BRL", "SAIDA");

    ArgumentCaptor<Transacao> captor = ArgumentCaptor.forClass(Transacao.class);
    verify(repositorio).salvar(captor.capture());

    Assertions.assertEquals(StatusTransacao.REJEITADA, captor.getValue().getStatus());
  }

  @Test
  public void deveValidarSaldoParaTransferencia() {
    RepositorioDeTransacao repositorio = Mockito.mock(RepositorioDeTransacao.class);
    ValidadorDeSaldo validador = Mockito.mock(ValidadorDeSaldo.class);
    ServicoDeCotacao cotacao = Mockito.mock(ServicoDeCotacao.class);

    when(validador.saldoEhSuficiente(any(), any())).thenReturn(false);

    ProcessarTransacao useCase = new ProcessarTransacao(repositorio, validador, cotacao);

    useCase.executar("id-transf", "cpf-1", new BigDecimal("500.00"), "BRL", "TRANSFERENCIA");

    ArgumentCaptor<Transacao> captor = ArgumentCaptor.forClass(Transacao.class);
    verify(repositorio).salvar(captor.capture());

    Assertions.assertEquals(StatusTransacao.REJEITADA, captor.getValue().getStatus());

    verify(validador).saldoEhSuficiente(eq("cpf-1"), any());
  }
}
