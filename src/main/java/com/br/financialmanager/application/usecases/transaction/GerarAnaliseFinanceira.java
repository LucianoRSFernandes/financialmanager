package com.br.financialmanager.application.usecases.transaction;

import com.br.financialmanager.application.gateways.transaction.RepositorioDeTransacao;
import com.br.financialmanager.domain.transaction.ResumoDiario;
import com.br.financialmanager.domain.transaction.TipoTransacao;
import com.br.financialmanager.domain.transaction.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class GerarAnaliseFinanceira {

  private final RepositorioDeTransacao repositorio;

  public GerarAnaliseFinanceira(RepositorioDeTransacao repositorio) {
    this.repositorio = repositorio;
  }

  public List<ResumoDiario> executar(int mes, int ano) {
    List<Transacao> todas = repositorio.listarTodas();

    Map<LocalDate, List<Transacao>> transacoesPorDia = todas.stream()
      .filter(t -> t.getDataCriacao() != null)
      .filter(t -> !t.isApenasRegistro())
      .filter(t -> t.getDataCriacao().getMonthValue() == mes && t.getDataCriacao().getYear() == ano)
      .collect(Collectors.groupingBy(t -> t.getDataCriacao().toLocalDate()));

    Map<LocalDate, ResumoDiario> resumoOrdenado = new TreeMap<>();

    transacoesPorDia.forEach((data, lista) -> {
      BigDecimal entrada = lista.stream()
        .filter(t -> t.getTipo() == TipoTransacao.ENTRADA)
        .map(Transacao::getValorBrl)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

      BigDecimal saida = lista.stream()
        .filter(t -> t.getTipo() == TipoTransacao.SAIDA)
        .map(Transacao::getValorBrl)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

      BigDecimal saldo = entrada.subtract(saida);

      resumoOrdenado.put(data, new ResumoDiario(data, saida, entrada, saldo));
    });

    return resumoOrdenado.values().stream().toList();
  }
}
