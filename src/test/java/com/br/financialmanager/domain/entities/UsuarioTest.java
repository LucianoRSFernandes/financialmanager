package com.br.financialmanager.domain.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class UsuarioTest {

  @Test
  public void naoDeveCadastrarUsuarioCpfInvalido(){
    Assertions.assertThrows(IllegalArgumentException.class,
      () -> new Usuario("12345678900", "Eva", LocalDate.parse("1999-11-11"),
        "email@email.com", "123456"));

    Assertions.assertThrows(IllegalArgumentException.class,
      () -> new Usuario("", "Eva", LocalDate.parse("1999-11-11"),
        "email@email.com", "123456"));
  }

  @Test
  public void criarUsuarioComCriadoorDeUsuario() {
    CriadorDeUsuario criador = new CriadorDeUsuario();

    Usuario usuario = criador.nomeCpfNascimento("Yui", "111.444.777-35",
      LocalDate.parse("2000-12-25"));

    Assertions.assertEquals(usuario.getNome(), "Yui");
  }
}
