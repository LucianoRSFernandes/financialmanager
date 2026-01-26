package com.br.financialmanager.domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transacao {
  private String id;
  private String usuarioId;

  private BigDecimal valorOriginal;
  private String moeda;
  private TipoTransacao tipo;

  private BigDecimal valorBrl;
  private BigDecimal taxaConversao;

  private StatusTransacao status;
  private LocalDateTime dataCriacao;

   public Transacao(String usuarioId, BigDecimal valorOriginal, String moeda, TipoTransacao tipo) {
    this.id = UUID.randomUUID().toString();
    this.usuarioId = usuarioId;
    this.valorOriginal = valorOriginal;
    this.moeda = moeda != null ? moeda.toUpperCase() : "BRL";
    this.tipo = tipo;
    this.status = StatusTransacao.PENDENTE;
    this.dataCriacao = LocalDateTime.now();

    if ("BRL".equals(this.moeda)) {
      this.taxaConversao = BigDecimal.ONE;
      this.valorBrl = valorOriginal;
    }
  }

  public Transacao(String id, String usuarioId, BigDecimal valorOriginal, String moeda,
                   TipoTransacao tipo, BigDecimal valorBrl, BigDecimal taxaConversao,
                   StatusTransacao status, LocalDateTime dataCriacao) {
    this.id = id;
    this.usuarioId = usuarioId;
    this.valorOriginal = valorOriginal;
    this.moeda = moeda;
    this.tipo = tipo;
    this.valorBrl = valorBrl;
    this.taxaConversao = taxaConversao;
    this.status = status;
    this.dataCriacao = dataCriacao;
  }

  public void atualizarValoresConvertidos(BigDecimal taxa, BigDecimal valorEmReais) {
    this.taxaConversao = taxa;
    this.valorBrl = valorEmReais;
  }

  public void definirStatus(StatusTransacao novoStatus) {
    this.status = novoStatus;
  }

  public String getId() { return id; }
  public String getUsuarioId() { return usuarioId; }
  public BigDecimal getValorOriginal() { return valorOriginal; }
  public String getMoeda() { return moeda; }
  public TipoTransacao getTipo() { return tipo; }
  public BigDecimal getValorBrl() { return valorBrl; }
  public BigDecimal getTaxaConversao() { return taxaConversao; }
  public StatusTransacao getStatus() { return status; }
  public LocalDateTime getDataCriacao() { return dataCriacao; }
}
