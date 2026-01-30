package com.br.financialmanager.application.usecases.user;

import com.br.financialmanager.application.gateways.user.RepositorioDeUsuario;
import com.br.financialmanager.domain.entities.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CriarUsuario {

  private final RepositorioDeUsuario repositorio;
  private final PasswordEncoder passwordEncoder;

  public CriarUsuario(RepositorioDeUsuario repositorio, PasswordEncoder passwordEncoder) {
    this.repositorio = repositorio;
    this.passwordEncoder = passwordEncoder;
  }

  public Usuario executar(Usuario usuario) {
    if (repositorio.existePorCpf(usuario.getCpf())) {
      throw new IllegalArgumentException("CPF já cadastrado no sistema.");
    }

    String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
    Usuario usuarioParaSalvar = new Usuario(
      usuario.getCpf(),
      usuario.getNome(),
      usuario.getNascimento(),
      usuario.getEmail(),
      senhaCriptografada
    );

    return repositorio.cadastrarUsuario(usuarioParaSalvar);
  }
}
