package com.br.financialmanager.config;

import com.br.financialmanager.application.gateways.user.LeitorDeArquivo;
import com.br.financialmanager.application.gateways.user.RepositorioDeUsuario;
import com.br.financialmanager.application.usecases.user.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsuarioConfig {

  @Bean
  CriarUsuario criarUsuario(RepositorioDeUsuario repositorioDeUsuario, PasswordEncoder passwordEncoder) {
    return new CriarUsuario(repositorioDeUsuario, passwordEncoder);
  }

  @Bean
  ListarUsuarios listarUsuarios(RepositorioDeUsuario repositorioDeUsuario) {
    return new ListarUsuarios(repositorioDeUsuario);
  }

  @Bean
  AlterarUsuario alteraUsuario(RepositorioDeUsuario repositorioDeUsuario){
    return new AlterarUsuario(repositorioDeUsuario);
  }

  @Bean
  ExcluirUsuario excluiUsuario(RepositorioDeUsuario repositorioDeUsuario){
    return new ExcluirUsuario(repositorioDeUsuario);
  }

  @Bean
  ImportarUsuarios importarUsuarios(RepositorioDeUsuario repositorio,
                                    LeitorDeArquivo leitor,
                                    PasswordEncoder encoder) {
    return new ImportarUsuarios(repositorio, leitor, encoder);
  }
}
