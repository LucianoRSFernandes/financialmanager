package com.br.financialmanager.domain.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class UsuarioTest {

  @Test
  public void naoDeveCadastrarUsuarioCpfInvalido(){
    Assertions.assertThrows(IllegalArgumentException.class,
      () -> new Usuario("12345678900", "Eva", LocalDate.parse("1999-11-11"),
      "email@email.com"));

    Assertions.assertThrows(IllegalArgumentException.class,
      () -> new Usuario("", "Eva", LocalDate.parse("1999-10-11"),
      "email@email.com"));
  }

  @Test
  public void criarUsuarioComCriadoorDeUsuario() {
    CriadorDeUsuario criador = new CriadorDeUsuario();
    Usuario usuario = criador.nomeCpfNascimento("Yui", "159.123.147-78",
      LocalDate.parse("2000-12-25"));

    Assertions.assertEquals(usuario.getNome(), "Yui");

  }

}
