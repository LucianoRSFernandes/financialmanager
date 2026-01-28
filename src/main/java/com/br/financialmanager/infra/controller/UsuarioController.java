package com.br.financialmanager.infra.controller;

import com.br.financialmanager.application.usecases.user.*;
import com.br.financialmanager.domain.entities.Usuario;
import com.br.financialmanager.infra.controller.dto.LoginDto;
import com.br.financialmanager.infra.gateways.http.ContaMockDto;
import com.br.financialmanager.infra.gateways.http.MockApiClient;
import com.br.financialmanager.infra.persistence.UsuarioEntity;
import com.br.financialmanager.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
  private final ImportarUsuarios importarUsuarios;
  private final MockApiClient mockApiClient;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;

  public UsuarioController(
    CriarUsuario criarUsuario,
    ListarUsuarios listarUsuarios,
    AlterarUsuario alteraUsuario,
    ExcluirUsuario excluirUsuario,
    TokenService tokenService,
    ImportarUsuarios importarUsuarios,
    MockApiClient mockApiClient,
    AuthenticationManager authenticationManager,
    PasswordEncoder passwordEncoder) {
    this.criarUsuario = criarUsuario;
    this.listarUsuarios = listarUsuarios;
    this.alteraUsuario = alteraUsuario;
    this.excluiUsuario = excluirUsuario;
    this.tokenService = tokenService;
    this.importarUsuarios = importarUsuarios;
    this.mockApiClient = mockApiClient;
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody @Valid LoginDto dto) {
    try {
      var usernamePassword = new UsernamePasswordAuthenticationToken(dto.cpf(), dto.senha());
      var auth = this.authenticationManager.authenticate(usernamePassword);
      var usuario = (UsuarioEntity) auth.getPrincipal();

      Usuario usuarioDomain = new Usuario(
        usuario.getCpf(),
        usuario.getNome(),
        usuario.getNascimento(),
        usuario.getEmail(),
        usuario.getSenha()
      );

      var token = tokenService.gerarToken(usuarioDomain);
      return ResponseEntity.ok(token);

    } catch (org.springframework.security.core.AuthenticationException e) {
      // Log para debug
      System.err.println("❌ Erro de Autenticação: " + e.getMessage());
      return ResponseEntity.status(401).body("Falha na autenticação: " + e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().body("Erro ao processar login");
    }
  }

  @PostMapping
  public UsuarioDto cadastrarUsuario(@RequestBody @Valid UsuarioDto dto) {
    String senhaCriptografada = passwordEncoder.encode(dto.senha());

    Usuario salvo = criarUsuario.cadastrarUsuario(new Usuario(
      dto.cpf(),
      dto.nome(),
      dto.nascimento(),
      dto.email(),
      senhaCriptografada
    ));

    return new UsuarioDto(
      salvo.getCpf(),
      salvo.getNome(),
      salvo.getNascimento(),
      salvo.getEmail(),
      salvo.getSenha());
  }

  @GetMapping
  public List<UsuarioDto> listarUsuario() {
    return listarUsuarios.obterTodosUsuarios().stream()
      .map(u -> new UsuarioDto(u.getCpf(),
        u.getNome(), u.getNascimento(), u.getEmail(), u.getSenha()))
      .collect(Collectors.toList());
  }

  @PutMapping("/{cpf}")
  public ResponseEntity<UsuarioDto> atualizarUsuario(@PathVariable String cpf, @RequestBody @Valid UsuarioDto dto) {
    String senhaCriptografada = passwordEncoder.encode(dto.senha());

    Usuario usuarioParaAtualizar = new Usuario(
      cpf,
      dto.nome(),
      dto.nascimento(),
      dto.email(),
      senhaCriptografada
    );

    Usuario atualizado = alteraUsuario.alteraDadosUsuario(cpf, usuarioParaAtualizar);

    if (atualizado == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(new UsuarioDto(
      atualizado.getCpf(),
      atualizado.getNome(),
      atualizado.getNascimento(),
      atualizado.getEmail(),
      atualizado.getSenha()
    ));
  }

  @DeleteMapping("/{cpf}")
  public ResponseEntity<Void> excluirUsuario(@PathVariable String cpf) {
    excluiUsuario.excluirUsuario(cpf);
    return ResponseEntity.noContent().build();
  }

  @PostMapping(value = "/upload", consumes = "multipart/form-data")
  public ResponseEntity<String> uploadPlanilha(@RequestParam("file") MultipartFile file) {
    try {
      importarUsuarios.executar(file.getInputStream());
      return ResponseEntity.ok("Importação concluída com sucesso!");
    } catch (IOException e) {
      return ResponseEntity.badRequest().body("Erro ao ler arquivo: " + e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Erro na importação: " + e.getMessage());
    }
  }

  @GetMapping("/{id}/saldo")
  public ResponseEntity<ContaMockDto> consultarSaldo(@PathVariable String id) {
    var saldo = mockApiClient.buscarConta("1");
    return ResponseEntity.ok(saldo);
  }
}
