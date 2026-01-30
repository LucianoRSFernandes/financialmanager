package com.br.financialmanager.application.usecases.user;

import com.br.financialmanager.application.gateways.user.RepositorioDeUsuario;
import com.br.financialmanager.domain.entities.Usuario;

import java.util.List;

public class ListarUsuarios {

  private final RepositorioDeUsuario repositorio;

  public ListarUsuarios(RepositorioDeUsuario repositorio) {
    this.repositorio = repositorio;
  }

  public List<Usuario> executar() {
    return repositorio.listarTodos();
  }
}
