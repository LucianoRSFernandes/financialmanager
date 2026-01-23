package com.br.financialmanager.application.usecases;

import com.br.financialmanager.application.gateways.RepositorioDeUsuario;
import com.br.financialmanager.domain.entities.Usuario;

public class AlteraUsuario {
  private final RepositorioDeUsuario repositorio;

  public AlteraUsuario(RepositorioDeUsuario repositorio) {
    this.repositorio = repositorio;
  }

  public Usuario alteraDadosUsuario(Usuario usuario) {
    return repositorio.alteraUsuario(usuario.getCpf(), usuario.getEmail());
  }
}
