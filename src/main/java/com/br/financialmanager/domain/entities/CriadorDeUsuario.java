package com.br.financialmanager.domain.entities;

import java.time.LocalDate;

public class CriadorDeUsuario {

  private Usuario usuario;

  public Usuario nomeCpfNascimento (String nome, String cpf, LocalDate nascimento) {
    this.usuario = new Usuario(cpf, nome, nascimento, "", "123456");
    return this.usuario;
  }
}
