package com.br.financialmanager.application.usecases.user;

import com.br.financialmanager.application.gateways.user.RepositorioDeUsuario;
import com.br.financialmanager.domain.entities.Usuario;

public class AlterarUsuario {
  private final RepositorioDeUsuario repositorio;

  public AlterarUsuario (RepositorioDeUsuario repositorio) {
    this.repositorio = repositorio;
  }

  public Usuario alteraDadosUsuario(String cpf, Usuario usuario) {
    return repositorio.alteraUsuario(cpf, usuario);
  }
}
