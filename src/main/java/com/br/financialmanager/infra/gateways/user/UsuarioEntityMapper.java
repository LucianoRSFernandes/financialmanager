package com.br.financialmanager.infra.gateways.user;

import com.br.financialmanager.domain.entities.Usuario;
import com.br.financialmanager.infra.persistence.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioEntityMapper {

  public UsuarioEntity toEntity(Usuario usuario){
    return new UsuarioEntity(usuario.getCpf(), usuario.getNome(),
      usuario.getNascimento(), usuario.getEmail(), usuario.getSenha());
  }

  public Usuario toDomain(UsuarioEntity entity){
    return new Usuario(entity.getCpf(), entity.getNome(), entity.getNascimento(),
      entity.getEmail(), entity.getSenha());
  }
}
