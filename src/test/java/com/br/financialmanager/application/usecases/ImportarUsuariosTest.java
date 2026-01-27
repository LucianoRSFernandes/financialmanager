package com.br.financialmanager.application.usecases;

import com.br.financialmanager.application.gateways.user.LeitorDeArquivo;
import com.br.financialmanager.application.gateways.user.RepositorioDeUsuario;
import com.br.financialmanager.application.usecases.user.ImportarUsuarios;
import com.br.financialmanager.domain.entities.Usuario;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.InputStream;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ImportarUsuariosTest {

  @Test
  public void deveCadastrarCadaUsuarioLido() {
    RepositorioDeUsuario repositorio = Mockito.mock(RepositorioDeUsuario.class);
    LeitorDeArquivo leitor = Mockito.mock(LeitorDeArquivo.class);

    Usuario u1 = Mockito.mock(Usuario.class);
    Usuario u2 = Mockito.mock(Usuario.class);
    when(leitor.lerUsuarios(any())).thenReturn(Arrays.asList(u1, u2));

    ImportarUsuarios useCase = new ImportarUsuarios(repositorio, leitor);

    useCase.executar(Mockito.mock(InputStream.class));

    verify(repositorio, times(2)).cadastrarUsuario(any());
  }
}
