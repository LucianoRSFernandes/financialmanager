package com.br.financialmanager.application.usecases.user;

import com.br.financialmanager.application.gateways.user.RepositorioDeUsuario;

public class ExcluirUsuario {

  private final RepositorioDeUsuario repositorio;

  public ExcluirUsuario(RepositorioDeUsuario repositorio) {
    this.repositorio = repositorio;
  }

  public void executar(String cpf) {
    repositorio.excluiUsuario(cpf);
  }
}
