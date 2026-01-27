package com.br.financialmanager.application.usecases;

import com.br.financialmanager.application.gateways.user.RepositorioDeUsuario;
import com.br.financialmanager.application.usecases.user.ListarUsuarios;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListarUsuariosTest {

  @Test
  public void deveChamarRepositorioParaListar() {
    RepositorioDeUsuario repositorio = Mockito.mock(RepositorioDeUsuario.class);
    ListarUsuarios useCase = new ListarUsuarios(repositorio);

    when(repositorio.listarTodos()).thenReturn(Collections.emptyList());

    useCase.obterTodosUsuarios();

    verify(repositorio).listarTodos();
  }
}
