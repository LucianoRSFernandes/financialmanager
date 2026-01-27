package com.br.financialmanager.application.gateways.user;

import com.br.financialmanager.domain.entities.Usuario;

import java.io.InputStream;
import java.util.List;

public interface LeitorDeArquivo {
  List<Usuario> lerUsuarios(InputStream arquivo);
}
