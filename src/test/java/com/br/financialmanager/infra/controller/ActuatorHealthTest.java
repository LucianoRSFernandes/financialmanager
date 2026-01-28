package com.br.financialmanager.infra.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ActuatorHealthTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser(username = "monitoramento", roles = {"USER"})
  void healthCheckDeveRetornarStatusUpQuandoAutenticado() throws Exception {
    mockMvc.perform(get("/actuator/health"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("UP"));
  }

  @Test
  void healthCheckDeveRetornar403QuandoNaoAutenticado() throws Exception {
    mockMvc.perform(get("/actuator/health"))
      .andExpect(status().isForbidden());
  }
}
