package com.br.financialmanager.infra.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UsuarioDto(
  @NotBlank(message = "O CPF é obrigatório")
  @CpfValid(message = "CPF inválido (Erro na verificação dos dígitos)")
  String cpf,

  @NotBlank(message = "O nome é obrigatório")
  String nome,

  LocalDate nascimento,

  @NotBlank(message = "O email é obrigatório")
  @Email(message = "Formato de email inválido")
  String email,

  @NotBlank(message = "A senha é obrigatória")
  @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
  String senha
) {}
