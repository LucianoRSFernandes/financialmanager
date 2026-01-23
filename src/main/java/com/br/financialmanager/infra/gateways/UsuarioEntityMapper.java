package com.br.financialmanager.infra.gateways;

import com.br.financialmanager.domain.entities.Usuario;
import com.br.financialmanager.infra.persistence.UsuarioEntity;

public class UsuarioEntityMapper {

  public UsuarioEntity toEntity(Usuario usuario){
    return new UsuarioEntity(usuario.getCpf(), usuario.getNome(),
      usuario.getNascimento(), usuario.getEmail());
  }

  public Usuario toDomain(UsuarioEntity entity){
    return new Usuario(entity.getCpf(), entity.getNome(), entity.getNascimento(),
      entity.getEmail());
  }
}
