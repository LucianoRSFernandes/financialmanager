package com.br.financialmanager.application.usecases;

import com.br.financialmanager.application.gateways.RepositorioDeUsuario;
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
