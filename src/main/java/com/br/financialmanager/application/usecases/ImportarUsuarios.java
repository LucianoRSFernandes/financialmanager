package com.br.financialmanager.application.usecases;

import com.br.financialmanager.application.gateways.LeitorDeArquivo;
import com.br.financialmanager.application.gateways.RepositorioDeUsuario;
import com.br.financialmanager.domain.entities.Usuario;

import java.io.InputStream;
import java.util.List;

public class ImportarUsuarios {

  private final RepositorioDeUsuario repositorio;
  private final LeitorDeArquivo leitor;

  public ImportarUsuarios(RepositorioDeUsuario repositorio, LeitorDeArquivo leitor) {
    this.repositorio = repositorio;
    this.leitor = leitor;
  }

  public void executar(InputStream arquivo) {
    List<Usuario> usuarios = leitor.lerUsuarios(arquivo);

    for (Usuario usuario : usuarios) {
      try {
        repositorio.cadastrarUsuario(usuario);
        System.out.println("✅ Usuário importado: " + usuario.getNome());
      } catch (Exception e) {
        System.err.println("⚠️ Falha ao importar " + usuario.getNome() + ": " + e.getMessage());
      }
    }
  }
}
