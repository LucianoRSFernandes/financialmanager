package com.br.financialmanager.infra.controller;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<CpfValid, String> {

  @Override
  public boolean isValid(String cpf, ConstraintValidatorContext context) {

    if (cpf == null) {
      return true;
    }

    String cpfLimpo = cpf.replaceAll("\\D", "");

    if (cpfLimpo.length() != 11 || isTudoIgual(cpfLimpo)) {
      return false;
    }

    return validarDigitos(cpfLimpo);
  }

  private boolean isTudoIgual(String cpf) {
    return cpf.chars().distinct().count() == 1;
  }

  private boolean validarDigitos(String cpf) {
    int soma = 0;
    int peso = 10;

    for (int i = 0; i < 9; i++) {
      int num = cpf.charAt(i) - '0';
      soma += num * peso--;
    }

    int resto = 11 - (soma % 11);
    char dig10 = (resto >= 10) ? '0' : (char) (resto + '0');

    soma = 0;
    peso = 11;

    for (int i = 0; i < 10; i++) {
      int num = cpf.charAt(i) - '0';
      soma += num * peso--;
    }

    resto = 11 - (soma % 11);
    char dig11 = (resto >= 10) ? '0' : (char) (resto + '0');

    return dig10 == cpf.charAt(9) && dig11 == cpf.charAt(10);
  }
}
