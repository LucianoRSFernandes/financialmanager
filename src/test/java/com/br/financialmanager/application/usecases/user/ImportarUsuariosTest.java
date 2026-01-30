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

    Usuario u1 = new Usuario("053.511.518-04", "Usuario Com Erro",
      LocalDate.now(), "email1@teste.com", "senha123");

    Usuario u2 = new Usuario("923.834.720-42", "Usuario Sucesso",
      LocalDate.now(), "email2@teste.com", "senha123");

    when(leitor.lerUsuarios(any())).thenReturn(Arrays.asList(u1, u2));

    when(encoder.encode("senha123")).thenReturn("hashSeguro123");

    doThrow(new RuntimeException("Erro ao salvar usuário"))
      .when(repositorio).cadastrarUsuario(argThat(u -> u.getNome().equals("Usuario Com Erro")));

    ImportarUsuarios useCase = new ImportarUsuarios(repositorio, leitor, encoder);

    useCase.executar(Mockito.mock(InputStream.class));

    verify(repositorio, times(1)).cadastrarUsuario(argThat(u -> u.getNome()
      .equals("Usuario Com Erro")));

    verify(repositorio, times(1))
      .cadastrarUsuario(argThat(u -> u.getNome().equals("Usuario Sucesso")));
  }
}
