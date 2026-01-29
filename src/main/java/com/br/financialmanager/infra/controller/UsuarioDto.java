package com.br.financialmanager.infra.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UsuarioDto(
  @NotBlank(message = "O CPF é obrigatório")
  @CpfValid(message = "CPF inválido (Erro na verificação dos dígitos)")
  String cpf,

  @NotBlank(message = "O nome é obrigatório")
  String nome,

  @NotNull(message = "A data de nascimento é obrigatória")
  @Past(message = "A data de nascimento deve ser no passado")
  LocalDate nascimento,

  @NotBlank(message = "O email é obrigatório")
  @Email(message = "Formato de email inválido")
  String email,

  @NotBlank(message = "A senha é obrigatória")
  @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
  String senha
) {}
