package com.br.financialmanager.application.usecases.user;

import com.br.financialmanager.application.gateways.user.RepositorioDeUsuario;
import com.br.financialmanager.domain.entities.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CriarUsuarioTest {

  @Test
  public void deveCriarUsuarioQuandoCpfNaoExiste() {

    RepositorioDeUsuario repositorio = Mockito.mock(RepositorioDeUsuario.class);
    PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);

    String cpfValido = "111.444.777-35";
    Usuario usuarioEntrada = new Usuario(cpfValido, "Teste", LocalDate.now(), "teste@email.com", "senha123");

    when(repositorio.existePorCpf(cpfValido)).thenReturn(false); // CPF não existe
    when(encoder.encode("senha123")).thenReturn("hashSegura123");
    when(repositorio.cadastrarUsuario(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);

    CriarUsuario useCase = new CriarUsuario(repositorio, encoder);
    Usuario criado = useCase.executar(usuarioEntrada);
    Assertions.assertNotNull(criado);
    Assertions.assertEquals("hashSegura123", criado.getSenha()); // Confirma que a senha foi criptografada

    verify(repositorio, times(1)).cadastrarUsuario(any(Usuario.class));
  }

  @Test
  public void naoDeveCriarUsuarioComCpfDuplicado() {

    RepositorioDeUsuario repositorio = Mockito.mock(RepositorioDeUsuario.class);
    PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);

    String cpfDuplicado = "111.444.777-35";
    Usuario usuarioEntrada = new Usuario(cpfDuplicado, "Duplicado", LocalDate.now(), "duplicado@email.com", "senha123");

    when(repositorio.existePorCpf(cpfDuplicado)).thenReturn(true);

    CriarUsuario useCase = new CriarUsuario(repositorio, encoder);

    IllegalArgumentException exception = Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> useCase.executar(usuarioEntrada)
    );

    Assertions.assertEquals("CPF já cadastrado no sistema.", exception.getMessage());

    verify(repositorio, never()).cadastrarUsuario(any());
  }
}
