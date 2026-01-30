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
  private CategoriaTransacao categoria;
  private BigDecimal valorBrl;
  private BigDecimal taxaConversao;
  private StatusTransacao status;
  private LocalDateTime dataCriacao;
  private boolean apenasRegistro;

  public Transacao(String usuarioId, BigDecimal valorOriginal, String moeda,
                   TipoTransacao tipo, CategoriaTransacao categoria, boolean apenasRegistro) {
    this.id = UUID.randomUUID().toString();
    this.usuarioId = usuarioId;
    this.valorOriginal = valorOriginal;
    this.moeda = moeda != null ? moeda.toUpperCase() : "BRL";
    this.tipo = tipo;
    this.categoria = categoria != null ? categoria : CategoriaTransacao.OUTROS;
    this.apenasRegistro = apenasRegistro;
    this.status = StatusTransacao.PENDENTE;
    this.dataCriacao = LocalDateTime.now();

    if ("BRL".equals(this.moeda)) {
      this.taxaConversao = BigDecimal.ONE;
      this.valorBrl = valorOriginal;
    }
  }

  public Transacao(String id, String usuarioId, BigDecimal valorOriginal, String moeda,
                   TipoTransacao tipo, BigDecimal valorBrl, BigDecimal taxaConversao,
                   StatusTransacao status, LocalDateTime dataCriacao,
                   CategoriaTransacao categoria, boolean apenasRegistro) {
    this.id = id;
    this.usuarioId = usuarioId;
    this.valorOriginal = valorOriginal;
    this.moeda = moeda;
    this.tipo = tipo;
    this.valorBrl = valorBrl;
    this.taxaConversao = taxaConversao;
    this.status = status;
    this.dataCriacao = dataCriacao;
    this.categoria = categoria;
    this.apenasRegistro = apenasRegistro;
  }

  public boolean isApenasRegistro() { return apenasRegistro; }

  public void atualizarValoresConvertidos(BigDecimal taxa, BigDecimal valorEmReais) {
    this.taxaConversao = taxa;
    this.valorBrl = valorEmReais;
  }

  public void definirStatus(StatusTransacao novoStatus) {
    this.status = novoStatus;
  }

  public void cancelar() {
    if (this.status != StatusTransacao.PENDENTE) {
      throw new IllegalStateException("Apenas transações pendentes podem ser canceladas.");
    }
    this.status = StatusTransacao.CANCELADA;
  }

  public String getId() { return id; }
  public String getUsuarioId() { return usuarioId; }
  public BigDecimal getValorOriginal() { return valorOriginal; }
  public String getMoeda() { return moeda; }
  public TipoTransacao getTipo() { return tipo; }
  public CategoriaTransacao getCategoria() { return categoria; } // Novo Getter
  public BigDecimal getValorBrl() { return valorBrl; }
  public BigDecimal getTaxaConversao() { return taxaConversao; }
  public StatusTransacao getStatus() { return status; }
  public LocalDateTime getDataCriacao() { return dataCriacao; }

  public void setCategoria(CategoriaTransacao categoria) {
    this.categoria = categoria;
  }
}
