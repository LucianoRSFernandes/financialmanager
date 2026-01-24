package com.br.financialmanager.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<TransacaoEntity, String> {
}
