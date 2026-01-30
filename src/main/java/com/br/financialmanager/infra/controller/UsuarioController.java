package com.br.financialmanager.infra.controller;

import com.br.financialmanager.application.usecases.user.*;
import com.br.financialmanager.domain.entities.Usuario;
import com.br.financialmanager.infra.controller.dto.LoginDto;
import com.br.financialmanager.infra.gateways.http.ContaMockDto;
import com.br.financialmanager.infra.gateways.http.MockApiClient;
import com.br.financialmanager.infra.persistence.UsuarioEntity;
import com.br.financialmanager.infra.security.TokenService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

  private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);
  private final CriarUsuario criarUsuario;
  private final ListarUsuarios listarUsuarios;
  private final AlterarUsuario alteraUsuario;
  private final ExcluirUsuario excluiUsuario;
  private final ImportarUsuarios importarUsuarios;
  private final TokenService tokenService;
  private final MockApiClient mockApiClient;
  private final AuthenticationManager authenticationManager;

  public UsuarioController(
    CriarUsuario criarUsuario,
    ListarUsuarios listarUsuarios,
    AlterarUsuario alteraUsuario,
    ExcluirUsuario excluiUsuario,
    ImportarUsuarios importarUsuarios,
    TokenService tokenService,
    MockApiClient mockApiClient,
    AuthenticationManager authenticationManager) {
    this.criarUsuario = criarUsuario;
    this.listarUsuarios = listarUsuarios;
    this.alteraUsuario = alteraUsuario;
    this.excluiUsuario = excluiUsuario;
    this.importarUsuarios = importarUsuarios;
    this.tokenService = tokenService;
    this.mockApiClient = mockApiClient;
    this.authenticationManager = authenticationManager;
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
      log.error("❌ Erro de Autenticação: {}", e.getMessage());
      return ResponseEntity.status(401).body("Falha na autenticação: " + e.getMessage());
    } catch (Exception e) {
      log.error("Erro inesperado no login", e);
      return ResponseEntity.badRequest().body("Erro ao processar login");
    }
  }

  @PostMapping
  public ResponseEntity<UsuarioDto> cadastrarUsuario(@RequestBody @Valid UsuarioDto dto) {

    Usuario novoUsuario = new Usuario(
      dto.cpf(),
      dto.nome(),
      dto.nascimento(),
      dto.email(),
      dto.senha()
    );

    Usuario salvo = criarUsuario.executar(novoUsuario);

    UsuarioDto responseDto = new UsuarioDto(
      salvo.getCpf(),
      salvo.getNome(),
      salvo.getNascimento(),
      salvo.getEmail(),
      salvo.getSenha()
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  @GetMapping
  public ResponseEntity<List<UsuarioDto>> listarUsuario() {
    List<UsuarioDto> lista = listarUsuarios.executar().stream() // Padronizado para 'executar'
      .map(u -> new UsuarioDto(u.getCpf(),
        u.getNome(), u.getNascimento(), u.getEmail(), u.getSenha()))
      .collect(Collectors.toList());

    return ResponseEntity.ok(lista);
  }

  @PutMapping("/{cpf}")
  public ResponseEntity<UsuarioDto> atualizarUsuario(@PathVariable String cpf, @RequestBody @Valid UsuarioDto dto) {
    Usuario usuarioParaAtualizar = new Usuario(
      cpf,
      dto.nome(),
      dto.nascimento(),
      dto.email(),
      dto.senha()
    );

    Usuario atualizado = alteraUsuario.executar(cpf, usuarioParaAtualizar);

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
    excluiUsuario.executar(cpf);
    return ResponseEntity.noContent().build();
  }

  @PostMapping(value = "/upload", consumes = "multipart/form-data")
  public ResponseEntity<String> uploadPlanilha(@RequestParam("file") MultipartFile file) {
    try {
      importarUsuarios.executar(file.getInputStream());
      return ResponseEntity.ok("Importação concluída com sucesso!");
    } catch (IOException e) {
      log.error("Erro ao ler arquivo de upload: {}", e.getMessage());
      return ResponseEntity.badRequest().body("Erro ao ler arquivo: " + e.getMessage());
    } catch (Exception e) {
      log.error("Erro na importação de usuários: {}", e.getMessage());
      return ResponseEntity.badRequest().body("Erro na importação: " + e.getMessage());
    }
  }

  @GetMapping("/{id}/saldo")
  public ResponseEntity<ContaMockDto> consultarSaldo(@PathVariable String id) {
    var listaContas = mockApiClient.buscarPorCpf(id);

    if (listaContas.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(listaContas.get(0));
  }
}
