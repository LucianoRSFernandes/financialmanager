package com.br.financialmanager.domain.entities;

import java.time.LocalDate;

public class Usuario {
  private String senha;
  private String cpf;
  private String nome;
  private LocalDate nascimento;
  private String email;

  public Usuario(String cpf, String nome, LocalDate nascimento, String email, String senha) {
    // 1. Validação de formato do CPF (Regex)
    if (cpf == null || !cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}")) {
      throw new IllegalArgumentException("CPF fora do padrão (Formato esperado: 000.000.000-00)");
    }

    // 2. Validação do Algoritmo do CPF (Lógica trazida para o domínio)
    if (!cpfValido(cpf)) {
      throw new IllegalArgumentException("CPF inválido (Dígitos verificadores incorretos)");
    }

    // 3. Validação de Senha
    if (senha == null || senha.trim().length() < 6) {
      throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres");
    }

    // 4. Validação de Data de Nascimento
    if (nascimento == null) {
      throw new IllegalArgumentException("A data de nascimento é obrigatória");
    }
    if (nascimento.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("A data de nascimento não pode ser no futuro");
    }

    this.cpf = cpf;
    this.nome = nome;
    this.nascimento = nascimento;
    this.email = email;
    this.senha = senha;
  }

  // --- Métodos Auxiliares de Validação de CPF ---

  private boolean cpfValido(String cpf) {
    String cpfLimpo = cpf.replaceAll("\\D", "");

    if (cpfLimpo.length() != 11 || isTudoIgual(cpfLimpo)) {
      return false;
    }

    return validarDigitos(cpfLimpo);
  }

  private boolean isTudoIgual(String cpf) {
    return cpf.chars().distinct().count() == 1;
  }

  private boolean validarDigitos(String cpf) {
    int soma = 0;
    int peso = 10;
    for (int i = 0; i < 9; i++) {
      int num = cpf.charAt(i) - '0';
      soma += num * peso--;
    }

    int resto = 11 - (soma % 11);
    char dig10 = (resto >= 10) ? '0' : (char) (resto + '0');

    soma = 0;
    peso = 11;
    for (int i = 0; i < 10; i++) {
      int num = cpf.charAt(i) - '0';
      soma += num * peso--;
    }

    resto = 11 - (soma % 11);
    char dig11 = (resto >= 10) ? '0' : (char) (resto + '0');

    return dig10 == cpf.charAt(9) && dig11 == cpf.charAt(10);
  }

  public String getCpf() { return cpf; }
  public void setCpf(String cpf) { this.cpf = cpf; }

  public String getNome() { return nome; }
  public void setNome(String nome) { this.nome = nome; }

  public LocalDate getNascimento() { return nascimento; }
  public void setNascimento(LocalDate nascimento) { this.nascimento = nascimento; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getSenha() { return senha; }
}
