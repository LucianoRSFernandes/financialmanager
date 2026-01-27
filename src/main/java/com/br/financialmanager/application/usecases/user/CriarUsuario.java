package com.br.financialmanager.application.usecases.user;

import com.br.financialmanager.application.gateways.user.RepositorioDeUsuario;
import com.br.financialmanager.domain.entities.Usuario;

public class CriarUsuario {
  private final RepositorioDeUsuario repositorio;

  public CriarUsuario(RepositorioDeUsuario repositorio) {
    this.repositorio = repositorio;
  }

  public Usuario cadastrarUsuario(Usuario usuario) {
    return repositorio.cadastrarUsuario(usuario);
  }
}
