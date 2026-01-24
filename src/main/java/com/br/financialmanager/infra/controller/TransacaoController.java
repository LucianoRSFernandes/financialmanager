package com.br.financialmanager.infra.controller;

import com.br.financialmanager.application.usecases.CriarTransacao;
import com.br.financialmanager.domain.transaction.Transacao;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

  private final CriarTransacao criarTransacao;

  public TransacaoController(CriarTransacao criarTransacao) {
    this.criarTransacao = criarTransacao;
  }

  @PostMapping
  public Transacao solicitarTransacao(@RequestBody TransacaoDto dto) {
    return criarTransacao.executar(dto.cpf(), dto.valor());
  }
}
