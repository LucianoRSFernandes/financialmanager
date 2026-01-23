package com.br.financialmanager.application.usecases;

import com.br.financialmanager.application.gateways.RepositorioDeUsuario;
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
