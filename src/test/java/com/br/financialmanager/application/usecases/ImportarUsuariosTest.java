package com.br.financialmanager.application.usecases;

import com.br.financialmanager.application.gateways.user.LeitorDeArquivo;
import com.br.financialmanager.application.gateways.user.RepositorioDeUsuario;
import com.br.financialmanager.application.usecases.user.ImportarUsuarios;
import com.br.financialmanager.domain.entities.Usuario;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.InputStream;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ImportarUsuariosTest {

  @Test
  public void deveCadastrarCadaUsuarioLidoComSenhaCriptografada() {
    RepositorioDeUsuario repositorio = Mockito.mock(RepositorioDeUsuario.class);
    LeitorDeArquivo leitor = Mockito.mock(LeitorDeArquivo.class);
    PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);

    Usuario u1 = new Usuario("123.456.789-00", "Teste1",
      null, "email1", "senhaPura1");
    Usuario u2 = new Usuario("123.456.789-01", "Teste2",
      null, "email2", "senhaPura2");

    when(leitor.lerUsuarios(any())).thenReturn(Arrays.asList(u1, u2));
    when(encoder.encode(anyString())).thenReturn("senhaCriptografada");

    ImportarUsuarios useCase = new ImportarUsuarios(repositorio, leitor, encoder);

    useCase.executar(Mockito.mock(InputStream.class));

    verify(repositorio, times(2)).cadastrarUsuario(any());

    verify(encoder, times(2)).encode(anyString());
  }
}
