package com.br.financialmanager.application.gateways;

import com.br.financialmanager.domain.entities.Usuario;

import java.util.List;

public interface RepositorioDeUsuario {
  Usuario cadastrarUsuario (Usuario usuario);

  List<Usuario> listarTodos();

  Usuario alteraUsuario(String cpf, String email);
}
