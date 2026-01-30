package com.br.financialmanager.infra.persistence;

import com.br.financialmanager.domain.transaction.CategoriaTransacao;
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

  @Enumerated(EnumType.STRING)
  private CategoriaTransacao categoria;

  private BigDecimal valorBrl;
  private BigDecimal taxaConversao;

  @Enumerated(EnumType.STRING)
  private StatusTransacao status;

  private LocalDateTime dataCriacao;

  private boolean apenasRegistro; 

  public TransacaoEntity() {}

  public TransacaoEntity(String id, String usuarioId, BigDecimal valorOriginal, String moeda,
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

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getUsuarioId() { return usuarioId; }
  public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

  public BigDecimal getValorOriginal() { return valorOriginal; }
  public void setValorOriginal(BigDecimal valorOriginal) { this.valorOriginal = valorOriginal; }

  public String getMoeda() { return moeda; }
  public void setMoeda(String moeda) { this.moeda = moeda; }

  public TipoTransacao getTipo() { return tipo; }
  public void setTipo(TipoTransacao tipo) { this.tipo = tipo; }

  public CategoriaTransacao getCategoria() { return categoria; }
  public void setCategoria(CategoriaTransacao categoria) { this.categoria = categoria; }

  public BigDecimal getValorBrl() { return valorBrl; }
  public void setValorBrl(BigDecimal valorBrl) { this.valorBrl = valorBrl; }

  public BigDecimal getTaxaConversao() { return taxaConversao; }
  public void setTaxaConversao(BigDecimal taxaConversao) { this.taxaConversao = taxaConversao; }

  public StatusTransacao getStatus() { return status; }
  public void setStatus(StatusTransacao status) { this.status = status; }

  public LocalDateTime getDataCriacao() { return dataCriacao; }
  public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

  public boolean isApenasRegistro() { return apenasRegistro; }
  public void setApenasRegistro(boolean apenasRegistro) { this.apenasRegistro = apenasRegistro; }
}
