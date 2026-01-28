package com.br.financialmanager.application.usecases.user;

import com.br.financialmanager.application.gateways.user.LeitorDeArquivo;
import com.br.financialmanager.application.gateways.user.RepositorioDeUsuario;
import com.br.financialmanager.domain.entities.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.InputStream;
import java.util.List;

public class ImportarUsuarios {

  private static final Logger log = LoggerFactory.getLogger(ImportarUsuarios.class);
  private final RepositorioDeUsuario repositorio;
  private final LeitorDeArquivo leitor;
  private final PasswordEncoder passwordEncoder;

  public ImportarUsuarios(
    RepositorioDeUsuario repositorio,
    LeitorDeArquivo leitor,
    PasswordEncoder passwordEncoder) {
    this.repositorio = repositorio;
    this.leitor = leitor;
    this.passwordEncoder = passwordEncoder;
  }

  public void executar(InputStream arquivo) {
    List<Usuario> usuarios = leitor.lerUsuarios(arquivo);

    for (Usuario usuario : usuarios) {
      try {
        String senhaHash = passwordEncoder.encode(usuario.getSenha());
        Usuario usuarioSeguro = new Usuario(
          usuario.getCpf(),
          usuario.getNome(),
          usuario.getNascimento(),
          usuario.getEmail(),
          senhaHash
        );

        repositorio.cadastrarUsuario(usuarioSeguro);
        log.info("✅ Usuário importado: {}", usuario.getNome());
      } catch (Exception e) {
        log.error("⚠️ Falha ao importar {}: {}", usuario.getNome(), e.getMessage());
      }
    }
  }
}
