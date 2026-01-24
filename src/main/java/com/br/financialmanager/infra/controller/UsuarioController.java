package com.br.financialmanager.infra.controller;

import com.br.financialmanager.application.usecases.AlterarUsuario;
import com.br.financialmanager.application.usecases.CriarUsuario;
import com.br.financialmanager.application.usecases.ExcluirUsuario;
import com.br.financialmanager.application.usecases.ListarUsuarios;
import com.br.financialmanager.domain.entities.Usuario;
import com.br.financialmanager.infra.persistence.UsuarioRepository;
import com.br.financialmanager.infra.security.TokenService;
import org.springframework.http.ResponseEntity;
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
  private final TokenService tokenService;
  private final UsuarioRepository usuarioRepository;

  public UsuarioController(
    CriarUsuario criarUsuario,
    ListarUsuarios listarUsuarios,
    AlterarUsuario alterarUsuario,
    ExcluirUsuario excluirUsuario,
    TokenService tokenService,
    UsuarioRepository usuarioRepository){
    this.criarUsuario = criarUsuario;
    this.listarUsuarios = listarUsuarios;
    this.alteraUsuario = alterarUsuario;
    this.excluiUsuario = excluirUsuario;
    this.tokenService = tokenService;
    this.usuarioRepository = usuarioRepository;
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody LoginDto dto) {
    var usuarioEntity = usuarioRepository.findByCpf(dto.cpf());

    if (usuarioEntity != null && usuarioEntity.getSenha().equals(dto.senha())) {

      Usuario usuarioDomain = new Usuario(
        usuarioEntity.getCpf(),
        usuarioEntity.getNome(),
        usuarioEntity.getNascimento(),
        usuarioEntity.getEmail(),
        usuarioEntity.getSenha()
      );

      var token = tokenService.gerarToken(usuarioDomain);
      return ResponseEntity.ok(token);
    }

    return ResponseEntity.badRequest().body("Usuário ou senha inválidos");
  }

  @PostMapping
  public UsuarioDto cadastrarUsuario(@RequestBody UsuarioDto dto) {
    Usuario salvo = criarUsuario.cadastrarUsuario(new Usuario(dto.cpf(), dto.nome(), dto.nascimento(),
      dto.email(), dto.senha()));

    return new UsuarioDto(salvo.getCpf(), salvo.getNome(), salvo.getNascimento(), salvo.getEmail(), salvo.getSenha());

  }

  @GetMapping
  public List<UsuarioDto> listarUsuario() {
    return listarUsuarios.obterTodosUsuarios().stream().map(u -> new UsuarioDto(u.getCpf(),
      u.getNome(), u.getNascimento(), u.getEmail(), u.getSenha())).collect(Collectors.toList());
  }

  @PutMapping("/{cpf}")
  public UsuarioDto atualizarUsuario(@PathVariable String cpf, @RequestBody UsuarioDto dto) {
    Usuario atualizado = alteraUsuario.alteraDadosUsuario(cpf,
      new Usuario(dto.cpf(), dto.nome(), dto.nascimento(), dto.email(), dto.senha()));
    return new UsuarioDto(atualizado.getCpf(), atualizado.getNome(), atualizado.getNascimento(), atualizado.getEmail(), atualizado.getSenha());
  }

  @DeleteMapping("/{cpf}")
  public void excluirUsuario(@PathVariable String cpf) {
    excluiUsuario.excluirUsuario(cpf);
  }
}
