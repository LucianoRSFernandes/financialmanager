package com.br.financialmanager.infra.persistence;

import com.br.financialmanager.domain.transaction.StatusTransacao;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
public class TransacaoEntity {
  @Id
  private String id;
  private String usuarioId;
  private BigDecimal valor;

  @Enumerated(EnumType.STRING)
  private StatusTransacao status;

  private LocalDateTime dataCriacao;

  public TransacaoEntity() {}

  public TransacaoEntity(
    String id,
    String usuarioId,
    BigDecimal valor,
    StatusTransacao status,
    LocalDateTime dataCriacao) {
    this.id = id;
    this.usuarioId = usuarioId;
    this.valor = valor;
    this.status = status;
    this.dataCriacao = dataCriacao;
  }

  public String getId() { return id; }
  public String getUsuarioId() { return usuarioId; }
  public BigDecimal getValor() { return valor; }
  public StatusTransacao getStatus() { return status; }
  public LocalDateTime getDataCriacao() { return dataCriacao; }
}
