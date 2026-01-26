package com.br.financialmanager.infra.controller;

import com.br.financialmanager.application.usecases.BuscarTransacao;
import com.br.financialmanager.application.usecases.CriarTransacao;
import com.br.financialmanager.application.usecases.GerarAnaliseFinanceira;
import com.br.financialmanager.application.usecases.ListarTransacoes;
import com.br.financialmanager.domain.transaction.ResumoDiario;
import com.br.financialmanager.domain.transaction.Transacao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

  private final CriarTransacao criarTransacao;
  private final ListarTransacoes listarTransacoes;
  private final BuscarTransacao buscarTransacao;
  private final GerarAnaliseFinanceira gerarAnalise;

  public TransacaoController(CriarTransacao criarTransacao,
                             ListarTransacoes listarTransacoes,
                             BuscarTransacao buscarTransacao,
                             GerarAnaliseFinanceira gerarAnalise) {
    this.criarTransacao = criarTransacao;
    this.listarTransacoes = listarTransacoes;
    this.buscarTransacao = buscarTransacao;
    this.gerarAnalise = gerarAnalise;
  }

  @PostMapping
  public Transacao solicitarTransacao(@RequestBody TransacaoDto dto) {
    String moeda = dto.moeda() != null ? dto.moeda() : "BRL";
    return criarTransacao.executar(dto.cpf(), dto.valor(), moeda, dto.tipo());
  }

  @GetMapping
  public List<Transacao> listar() {
    return listarTransacoes.obterTodasTransacoes();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Transacao> buscarPorId(@PathVariable String id) {
    Transacao transacao = buscarTransacao.buscarPorId(id);
    if (transacao != null) {
      return ResponseEntity.ok(transacao);
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/analise")
  public List<ResumoDiario> analiseMensal(@RequestParam int mes, @RequestParam int ano) {
    return gerarAnalise.executar(mes, ano);
  }
}
