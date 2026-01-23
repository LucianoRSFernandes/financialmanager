package com.br.financialmanager.application.usecases;

import com.br.financialmanager.application.gateways.RepositorioDeUsuario;
import com.br.financialmanager.domain.entities.Usuario;

import java.util.List;

public class ListarUsuarios {

  private final RepositorioDeUsuario repositorio;

  public ListarUsuarios(RepositorioDeUsuario repositorio) {
    this.repositorio = repositorio;
  }

  public List<Usuario> obterTodosUsuarios() {
    return this.repositorio.listarTodos();
  }
}
