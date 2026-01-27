package com.br.financialmanager.application.usecases;

import com.br.financialmanager.application.gateways.transaction.RepositorioDeTransacao;
import com.br.financialmanager.application.usecases.transaction.GerarAnaliseFinanceira;
import com.br.financialmanager.domain.transaction.ResumoDiario;
import com.br.financialmanager.domain.transaction.StatusTransacao;
import com.br.financialmanager.domain.transaction.TipoTransacao;
import com.br.financialmanager.domain.transaction.Transacao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

public class GerarAnaliseFinanceiraTest {

  @Test
  public void deveCalcularTotaisESaldoDiario() {
    RepositorioDeTransacao repositorio = Mockito.mock(RepositorioDeTransacao.class);

    LocalDateTime data = LocalDateTime.of(2025, 5, 20, 10, 0);

    Transacao t1 = new Transacao("1", "u1", BigDecimal.ZERO, "BRL",
      TipoTransacao.ENTRADA, new BigDecimal("100.00"), BigDecimal.ONE,
      StatusTransacao.APROVADA, data);
    Transacao t2 = new Transacao("2", "u1", BigDecimal.ZERO, "BRL",
      TipoTransacao.SAIDA, new BigDecimal("30.00"), BigDecimal.ONE,
      StatusTransacao.APROVADA, data);

    when(repositorio.listarTodas()).thenReturn(Arrays.asList(t1, t2));

    GerarAnaliseFinanceira useCase = new GerarAnaliseFinanceira(repositorio);

    List<ResumoDiario> resultado = useCase.executar(5, 2025);

    Assertions.assertEquals(1, resultado.size());
    ResumoDiario dia = resultado.get(0);

    Assertions.assertEquals(new BigDecimal("100.00"), dia.totalEntrada());
    Assertions.assertEquals(new BigDecimal("30.00"), dia.totalSaida());
    Assertions.assertEquals(new BigDecimal("70.00"), dia.saldoDoDia());
  }
}
