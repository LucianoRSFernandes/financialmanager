package com.br.financialmanager.domain.entities;

import java.time.LocalDate;

public class Usuario {
  private String cpf;
  private String nome;
  private LocalDate nascimento;
  private String email;

  public Usuario (String cpf, String nome, LocalDate nascimento, String email) {
    if (cpf == null || !cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}")) {
      throw new IllegalArgumentException("CPF fora do padrão");
    }
    this.cpf = cpf;
    this.nome = nome;
    this.nascimento = nascimento;
    this.email = email;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public LocalDate getNascimento() {
    return nascimento;
  }

  public void setNascimento(LocalDate nascimento) {
    this.nascimento = nascimento;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
