package com.br.financialmanager.config;

import com.br.financialmanager.application.gateways.RepositorioDeUsuario;
import com.br.financialmanager.application.usecases.CriarUsuario;
import com.br.financialmanager.infra.gateways.RepositorioDeUsuarioJpa;
import com.br.financialmanager.infra.gateways.UsuarioEntityMapper;
import com.br.financialmanager.infra.persistence.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsuarioConfig {

  @Bean
  CriarUsuario criarusuario(RepositorioDeUsuario repositorioDeUsuario) {
    return new CriarUsuario(repositorioDeUsuario);
  }

  @Bean
  RepositorioDeUsuarioJpa criarRepositorioJpa(UsuarioRepository repositorio, UsuarioEntityMapper mapper) {
    return new RepositorioDeUsuarioJpa(repositorio, mapper);
  }

  @Bean
  UsuarioEntityMapper retornaMapper() {
    return new UsuarioEntityMapper();

  }

}
