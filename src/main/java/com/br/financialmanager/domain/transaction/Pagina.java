package com.br.financialmanager.domain.transaction;

import java.util.List;

public record Pagina<T>(
  List<T> conteudo,
  int paginaAtual,
  int totalPaginas,
  long totalElementos
) {}
