package com.br.financialmanager.infra.controller;

import com.br.financialmanager.application.gateways.transaction.FiltroTransacao;
import com.br.financialmanager.application.usecases.transaction.*;
import com.br.financialmanager.domain.transaction.Pagina;
import com.br.financialmanager.domain.transaction.Transacao;
import com.br.financialmanager.infra.persistence.UsuarioRepository;
import com.br.financialmanager.infra.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransacaoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TransacaoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private CriarTransacao criarTransacao;

  @MockitoBean
  private ListarTransacoes listarTransacoes;

  @MockitoBean
  private BuscarTransacao buscarTransacao;

  @MockitoBean
  private GerarAnaliseFinanceira gerarAnalise;

  @MockitoBean
  private CancelarTransacao cancelarTransacao;

  @MockitoBean
  private TokenService tokenService;

  @MockitoBean
  private UsuarioRepository usuarioRepository;

  @Test
  public void deveListarComFiltrosCorretamente() throws Exception {
    Pagina<Transacao> paginaVazia = new Pagina<>(Collections.emptyList(), 0, 0, 0);
    when(listarTransacoes.executar(any(FiltroTransacao.class), anyInt(), anyInt()))
      .thenReturn(paginaVazia);

    mockMvc.perform(get("/transacoes")
        .param("usuarioId", "12345678900")
        .param("status", "PENDENTE")
        .param("tipo", "SAIDA")
        .param("pagina", "0")
        .param("tamanho", "10")
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());

    verify(listarTransacoes).executar(any(FiltroTransacao.class), eq(0), eq(10));
  }

  @Test
  public void deveRetornar204AoCancelarComSucesso() throws Exception {
    mockMvc.perform(patch("/transacoes/id-valido-123/cancelar"))
      .andExpect(status().isNoContent());

    verify(cancelarTransacao).executar("id-valido-123");
  }

  @Test
  public void deveRetornar400AoTentarCancelarTransacaoJaProcessada() throws Exception {
    doThrow(new IllegalStateException("Apenas transações pendentes podem ser canceladas."))
      .when(cancelarTransacao).executar("id-ja-aprovado");

    mockMvc.perform(patch("/transacoes/id-ja-aprovado/cancelar"))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void deveRetornar404AoTentarCancelarTransacaoInexistente() throws Exception {
    doThrow(new IllegalArgumentException("Transação não encontrada"))
      .when(cancelarTransacao).executar("id-inexistente");

    mockMvc.perform(patch("/transacoes/id-inexistente/cancelar"))
      .andExpect(status().isNotFound());
  }

  @Test
  public void deveCriarComCategoriaEspecifica() throws Exception {
    Map<String, Object> request = new HashMap<>();
    request.put("cpf", "123.456.789-00");
    request.put("valor", 100.00);
    request.put("tipo", "ENTRADA");
    request.put("categoria", "SALARIO");

    mockMvc.perform(post("/transacoes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isCreated());
  }

  @Test
  public void deveAceitarSemCategoriaEAssumirDefault() throws Exception {
    Map<String, Object> request = new HashMap<>();
    request.put("cpf", "123.456.789-00");
    request.put("valor", 50.00);
    request.put("tipo", "SAIDA");

    mockMvc.perform(post("/transacoes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isCreated());
  }

  @Test
  public void deveRetornar400QuandoCategoriaForInvalida() throws Exception {
    String jsonInvalido = """
        {
          "cpf": "123.456.789-00",
          "valor": 100.00,
          "tipo": "SAIDA",
          "categoria": "CATEGORIA_INEXISTENTE"
        }
        """;

    mockMvc.perform(post("/transacoes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonInvalido))
      .andExpect(status().isBadRequest());
  }
}
