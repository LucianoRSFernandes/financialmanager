package com.br.financialmanager.infra.controller;

import com.br.financialmanager.application.usecases.CriarUsuario;
import com.br.financialmanager.application.usecases.ListarUsuarios;
import com.br.financialmanager.domain.entities.Usuario;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

  private final CriarUsuario criarUsuario;
  private final ListarUsuarios listarUsuarios;

  public UsuarioController(CriarUsuario criarUsuario, ListarUsuarios listarUsuarios) {
    this.criarUsuario = criarUsuario;
    this.listarUsuarios = listarUsuarios;
  }

  @PostMapping
  public UsuarioDto cadastrarUsuario(@RequestBody UsuarioDto dto) {
    Usuario salvo = criarUsuario.cadastrarUsuario(new Usuario(dto.cpf(), dto.nome(), dto.nascimento(),
      dto.email()));

    return new UsuarioDto(salvo.getCpf(), salvo.getNome(), salvo.getNascimento(), salvo.getEmail());

  }

  @GetMapping
  public List<UsuarioDto> listarUsuario() {
    return listarUsuarios.obterTodosUsuarios().stream().map(u->new UsuarioDto(u.getCpf(),
      u.getNome(), u.getNascimento(), u.getEmail())).collect(Collectors.toList());
  }
}
