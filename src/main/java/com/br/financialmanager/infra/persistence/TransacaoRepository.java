package com.br.financialmanager.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransacaoRepository extends JpaRepository<
  TransacaoEntity, String>, JpaSpecificationExecutor<TransacaoEntity> {
}
