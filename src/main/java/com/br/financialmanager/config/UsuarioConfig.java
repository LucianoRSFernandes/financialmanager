package com.br.financialmanager.config;

import com.br.financialmanager.application.gateways.user.LeitorDeArquivo;
import com.br.financialmanager.application.gateways.user.RepositorioDeUsuario;
import com.br.financialmanager.application.usecases.user.*;
import com.br.financialmanager.infra.gateways.LeitorDeExcel;
import com.br.financialmanager.infra.gateways.RepositorioDeUsuarioJpa;
import com.br.financialmanager.infra.gateways.UsuarioEntityMapper;
import com.br.financialmanager.infra.persistence.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsuarioConfig {

  @Bean
  CriarUsuario criarusuario(RepositorioDeUsuario repositorioDeUsuario) {
    return new CriarUsuario(repositorioDeUsuario);
  }

  @Bean
  ListarUsuarios listarUsuarios(RepositorioDeUsuario repositorioDeUsuario) {
    return new ListarUsuarios(repositorioDeUsuario);
  }

  @Bean
  RepositorioDeUsuarioJpa criarRepositorioJpa(UsuarioRepository repositorio, UsuarioEntityMapper mapper) {
    return new RepositorioDeUsuarioJpa(repositorio, mapper);
  }

  @Bean
  UsuarioEntityMapper retornaMapper() {
    return new UsuarioEntityMapper();
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
  LeitorDeArquivo leitorDeArquivo() {
    return new LeitorDeExcel();
  }

  @Bean
  ImportarUsuarios importarUsuarios(RepositorioDeUsuario repositorio, LeitorDeArquivo leitor, PasswordEncoder encoder) {
    return new ImportarUsuarios(repositorio, leitor, encoder);
  }
}
