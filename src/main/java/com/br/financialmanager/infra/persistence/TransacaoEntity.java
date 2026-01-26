package com.br.financialmanager.infra.persistence;

import com.br.financialmanager.domain.transaction.StatusTransacao;
import com.br.financialmanager.domain.transaction.TipoTransacao;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
public class TransacaoEntity {
  @Id
  private String id;
  private String usuarioId;
  private BigDecimal valorOriginal;
  private String moeda;

  @Enumerated(EnumType.STRING)
  private TipoTransacao tipo;

  private BigDecimal valorBrl;
  private BigDecimal taxaConversao;

  @Enumerated(EnumType.STRING)
  private StatusTransacao status;

  private LocalDateTime dataCriacao;

  public TransacaoEntity() {}

  public TransacaoEntity(String id, String usuarioId, BigDecimal valorOriginal, String moeda,
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
