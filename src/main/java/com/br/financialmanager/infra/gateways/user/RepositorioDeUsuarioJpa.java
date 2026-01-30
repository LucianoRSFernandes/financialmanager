package com.br.financialmanager.infra.gateways.user;

import com.br.financialmanager.application.gateways.user.RepositorioDeUsuario;
import com.br.financialmanager.domain.entities.Usuario;
import com.br.financialmanager.infra.persistence.UsuarioEntity;
import com.br.financialmanager.infra.persistence.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RepositorioDeUsuarioJpa implements RepositorioDeUsuario {

  private final UsuarioRepository repositorio;
  private final UsuarioEntityMapper mapper;

  public RepositorioDeUsuarioJpa(UsuarioRepository repositorio, UsuarioEntityMapper mapper) {
    this.repositorio = repositorio;
    this.mapper = mapper;
  }

  @Override
  public Usuario cadastrarUsuario(Usuario usuario) {
    UsuarioEntity entity = mapper.toEntity(usuario);
    return mapper.toDomain(repositorio.save(entity));
  }

  @Override
  public boolean existePorCpf(String cpf) {
    return repositorio.findByCpf(cpf) != null;
  }

  @Override
  public List<Usuario> listarTodos() {
    return repositorio.findAll().stream()
      .map(mapper::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public Usuario alteraUsuario(String cpf, Usuario usuario) {
    UsuarioEntity entity = repositorio.findByCpf(cpf);

    if (entity != null) {
      UsuarioEntity atualizado = mapper.toEntity(usuario);
      atualizado.setId(entity.getId());

      return mapper.toDomain(repositorio.save(atualizado));
    }
    return null;
  }

  @Override
  public void excluiUsuario(String cpf) {
    UsuarioEntity entity = repositorio.findByCpf(cpf);

    if (entity != null) {
      repositorio.deleteById(entity.getId());
    }
  }
}
