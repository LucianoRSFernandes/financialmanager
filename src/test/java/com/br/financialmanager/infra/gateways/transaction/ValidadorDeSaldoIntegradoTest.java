package com.br.financialmanager.infra.gateways.transaction;

import com.br.financialmanager.infra.gateways.http.BrasilApiCambioDto;
import com.br.financialmanager.infra.gateways.http.BrasilApiClient;
import com.br.financialmanager.infra.gateways.http.ContaMockDto;
import com.br.financialmanager.infra.gateways.http.MockApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidadorDeSaldoIntegradoTest {

  @Mock
  private MockApiClient mockApiClient;

  @Mock
  private BrasilApiClient brasilApiClient;

  @InjectMocks
  private ValidadorDeSaldoIntegrado validador;

  @Test
  void deveConsultarMockApiComCpfLimpoENaoComIdFixo() {

    String cpfFormatado = "123.456.789-00";
    String cpfEsperadoNaApi = "12345678900";
    BigDecimal valorTransacao = new BigDecimal("50.00");

    when(brasilApiClient.buscarCotacao(anyString(), anyString()))
      .thenReturn(new BrasilApiCambioDto(Collections.emptyList()));

    ContaMockDto contaComSaldo = new ContaMockDto("1", new BigDecimal("100.00"), BigDecimal.ZERO);

    when(mockApiClient.buscarPorCpf(cpfEsperadoNaApi))
      .thenReturn(List.of(contaComSaldo));

    boolean resultado = validador.saldoEhSuficiente(cpfFormatado, valorTransacao);

    assertTrue(resultado);

    verify(mockApiClient).buscarPorCpf(cpfEsperadoNaApi);
  }
}
