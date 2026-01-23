package com.br.financialmanager.infra.controller;

import com.br.financialmanager.application.usecases.AlterarUsuario;
import com.br.financialmanager.application.usecases.CriarUsuario;
import com.br.financialmanager.application.usecases.ExcluirUsuario;
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
  private final AlterarUsuario alteraUsuario;
  private final ExcluirUsuario excluiUsuario;

  public UsuarioController(
    CriarUsuario criarUsuario,
    ListarUsuarios listarUsuarios,
    AlterarUsuario alterarUsuario,
    ExcluirUsuario excluirUsuario) {
    this.criarUsuario = criarUsuario;
    this.listarUsuarios = listarUsuarios;
    this.alteraUsuario = alterarUsuario;
    this.excluiUsuario = excluirUsuario;
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

  @PutMapping("/{cpf}")
  public UsuarioDto atualizarUsuario(@PathVariable String cpf, @RequestBody UsuarioDto dto) {
    Usuario atualizado = alteraUsuario.alteraDadosUsuario(cpf,
      new Usuario(dto.cpf(), dto.nome(), dto.nascimento(), dto.email()));
    return new UsuarioDto(atualizado.getCpf(), atualizado.getNome(), atualizado.getNascimento(), atualizado.getEmail());
  }

  @DeleteMapping("/{cpf}")
  public void excluirUsuario(@PathVariable String cpf) {
    excluiUsuario.excluirUsuario(cpf);
  }
}
