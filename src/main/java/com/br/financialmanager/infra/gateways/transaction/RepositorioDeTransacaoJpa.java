package com.br.financialmanager.infra.gateways.transaction;

import com.br.financialmanager.application.gateways.transaction.FiltroTransacao;
import com.br.financialmanager.application.gateways.transaction.RepositorioDeTransacao;
import com.br.financialmanager.domain.transaction.Pagina;
import com.br.financialmanager.domain.transaction.Transacao;
import com.br.financialmanager.infra.persistence.TransacaoEntity;
import com.br.financialmanager.infra.persistence.TransacaoRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioDeTransacaoJpa implements RepositorioDeTransacao {

  private final TransacaoRepository repository;

  public RepositorioDeTransacaoJpa(TransacaoRepository repository) {

    this.repository = repository;
  }

  @Override
  public Transacao salvar(Transacao transacao) {
    var entity = TransacaoMapper.toEntity(transacao);
    repository.save(entity);
    return TransacaoMapper.toDomain(entity);
  }

  @Override
  public List<Transacao> listarTodas() {
    return repository.findAll().stream()
      .map(TransacaoMapper::toDomain)
      .collect(Collectors.toList());
  }

  @Override
  public Transacao buscarPorId(String id) {
    return repository.findById(id)
      .map(TransacaoMapper::toDomain)
      .orElse(null);
  }

  @Override
  public Pagina<Transacao> listar(FiltroTransacao filtro, int pagina, int tamanho) {
    Specification<TransacaoEntity> spec = (
      root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (filtro.usuarioId() != null && !filtro.usuarioId().isEmpty()) {
        predicates.add(cb.equal(root.get("usuarioId"), filtro.usuarioId()));
      }
      if (filtro.status() != null) {
        predicates.add(cb.equal(root.get("status"), filtro.status()));
      }
      if (filtro.tipo() != null) {
        predicates.add(cb.equal(root.get("tipo"), filtro.tipo()));
      }
      if (filtro.dataInicio() != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("dataCriacao"),
          filtro.dataInicio().atStartOfDay()));
      }
      if (filtro.dataFim() != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("dataCriacao"),
          filtro.dataFim().atTime(23, 59, 59)));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };

    PageRequest pageRequest = PageRequest.of(pagina, tamanho,
      Sort.by("dataCriacao").descending());
    Page<TransacaoEntity> pageResult = repository.findAll(spec, pageRequest);

    List<Transacao> domainList = pageResult.getContent().stream()
      .map(TransacaoMapper::toDomain)
      .collect(Collectors.toList());

    return new Pagina<>(domainList, pageResult.getNumber(), pageResult
      .getTotalPages(), pageResult.getTotalElements());
  }
}
