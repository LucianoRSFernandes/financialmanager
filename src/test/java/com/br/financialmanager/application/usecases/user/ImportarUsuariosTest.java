package com.br.financialmanager.application.usecases.user;

import com.br.financialmanager.application.gateways.user.LeitorDeArquivo;
import com.br.financialmanager.application.gateways.user.RepositorioDeUsuario;
import com.br.financialmanager.domain.entities.Usuario;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class ImportarUsuariosTest {

  @Test
  public void deveLogarErroParaUsuarioInvalidoEContinuarImportacao() {

    RepositorioDeUsuario repositorio = Mockito.mock(RepositorioDeUsuario.class);
    LeitorDeArquivo leitor = Mockito.mock(LeitorDeArquivo.class);
    PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);

    // CPFs válidos para passar na validação do construtor
    Usuario u1 = new Usuario("053.511.518-04", "Usuario Com Erro",
      LocalDate.now(), "email1", "senha123");
    Usuario u2 = new Usuario("923.834.720-42", "Usuario Sucesso",
      LocalDate.now(), "email2", "senha123");

    when(leitor.lerUsuarios(any())).thenReturn(Arrays.asList(u1, u2));

    // CORREÇÃO: O hash retornado deve ter >= 6 caracteres para ser aceito pelo domínio Usuario
    when(encoder.encode("senha123")).thenReturn("hashSeguro123");

    // Simulamos que o repositório falha ao tentar salvar o primeiro usuário
    doThrow(new RuntimeException("Erro ao salvar usuário"))
      .when(repositorio).cadastrarUsuario(argThat(u -> u.getNome().equals("Usuario Com Erro")));

    ImportarUsuarios useCase = new ImportarUsuarios(repositorio, leitor, encoder);

    useCase.executar(Mockito.mock(InputStream.class));

    // Agora o repositório será chamado (e lançará a exceção simulada)
    verify(repositorio, times(1)).cadastrarUsuario(argThat(u -> u.getNome()
      .equals("Usuario Com Erro")));

    // Verifica que continuou e salvou o segundo usuário corretamente
    verify(repositorio, times(1))
      .cadastrarUsuario(argThat(u -> u.getNome().equals("Usuario Sucesso")));
  }
}
