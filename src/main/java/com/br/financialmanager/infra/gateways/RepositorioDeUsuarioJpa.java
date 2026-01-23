package com.br.financialmanager.infra.gateways;

import com.br.financialmanager.application.gateways.RepositorioDeUsuario;
import com.br.financialmanager.domain.entities.Usuario;
import com.br.financialmanager.infra.persistence.UsuarioEntity;
import com.br.financialmanager.infra.persistence.UsuarioRepository;

import java.util.List;

public class RepositorioDeUsuarioJpa implements RepositorioDeUsuario {

  private final UsuarioRepository repositorio;

  private final UsuarioEntityMapper mapper;

  public RepositorioDeUsuarioJpa (UsuarioRepository repositorio, UsuarioEntityMapper mapper) {
    this.repositorio = repositorio;
    this.mapper = mapper;
  }

  @Override
  public Usuario cadastrarUsuario(Usuario usuario) {
    UsuarioEntity entity = mapper.toEntity(usuario);
    repositorio.save(entity);
    return mapper.toDomain(entity);
  }

  @Override
  public List<Usuario> listarTodos() {
    //return repositorio.findAll();
    return null;
  }

}
