package com.br.financialmanager.domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transacao {
  private String id;
  private String usuarioId;
  private BigDecimal valor;
  private StatusTransacao status;
  private LocalDateTime dataCriacao;

  public Transacao(String usuarioId, BigDecimal valor) {
    this.id = UUID.randomUUID().toString();
    this.usuarioId = usuarioId;
    this.valor = valor;
    this.status = StatusTransacao.PENDENTE;
    this.dataCriacao = LocalDateTime.now();
  }

  public Transacao(String id, String usuarioId, BigDecimal valor, StatusTransacao status, LocalDateTime dataCriacao) {
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
