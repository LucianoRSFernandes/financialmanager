package com.br.financialmanager.infra.controller;

import com.br.financialmanager.application.gateways.transaction.FiltroTransacao;
import com.br.financialmanager.application.usecases.transaction.*;
import com.br.financialmanager.domain.transaction.*;
import com.br.financialmanager.infra.controller.dto.TransacaoRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transacoes")
@Tag(name = "Transações", description = "Endpoints para gestão de movimentações financeiras")
public class TransacaoController {

  private final CriarTransacao criarTransacao;
  private final ListarTransacoes listarTransacoes;
  private final BuscarTransacao buscarTransacao;
  private final GerarAnaliseFinanceira gerarAnalise;
  private final CancelarTransacao cancelarTransacao;

  public TransacaoController(CriarTransacao criarTransacao,
                             ListarTransacoes listarTransacoes,
                             BuscarTransacao buscarTransacao,
                             GerarAnaliseFinanceira gerarAnalise,
                             CancelarTransacao cancelarTransacao) {
    this.criarTransacao = criarTransacao;
    this.listarTransacoes = listarTransacoes;
    this.buscarTransacao = buscarTransacao;
    this.gerarAnalise = gerarAnalise;
    this.cancelarTransacao = cancelarTransacao;
  }

  @Operation(summary = "Listar transações", description = "Lista as transações com suporte a filtros e paginação")
  @GetMapping
  public ResponseEntity<Pagina<Transacao>> listar(
    @RequestParam(required = false) String usuarioId,
    @RequestParam(required = false) StatusTransacao status,
    @RequestParam(required = false) TipoTransacao tipo,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
    @RequestParam(defaultValue = "0") int pagina,
    @RequestParam(defaultValue = "10") int tamanho
  ) {
    FiltroTransacao filtro = new FiltroTransacao(usuarioId, dataInicio, dataFim, status, tipo);
    Pagina<Transacao> resultado = listarTransacoes.executar(filtro, pagina, tamanho);
    return ResponseEntity.ok(resultado);
  }

  @Operation(summary = "Cancelar transação", description = "Cancela uma transação pendente pelo ID")
  @PatchMapping("/{id}/cancelar")
  public ResponseEntity<Void> cancelar(@PathVariable String id) {
    try {
      cancelarTransacao.executar(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Buscar por ID", description = "Retorna os detalhes de uma transação específica")
  @GetMapping("/{id}")
  public ResponseEntity<Transacao> buscarPorId(@PathVariable String id) {
    Transacao transacao = buscarTransacao.buscarPorId(id);
    if (transacao != null) {
      return ResponseEntity.ok(transacao);
    }
    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Análise mensal", description = "Gera resumo diário de entradas e saídas para um mês específico")
  @GetMapping("/analise")
  public List<ResumoDiario> analiseMensal(@RequestParam int mes, @RequestParam int ano) {
    return gerarAnalise.executar(mes, ano);
  }

  @Operation(summary = "Exportar Excel", description = "Gera um relatório Excel (.xlsx) de todas as transações")
  @GetMapping("/exportar")
  public void exportarRelatorio(HttpServletResponse response) throws IOException {
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    String headerKey = "Content-Disposition";
    String headerValue = "attachment; filename=transacoes_" + System.currentTimeMillis() + ".xlsx";
    response.setHeader(headerKey, headerValue);

    List<Transacao> lista = listarTransacoes.obterTodasTransacoes();

    try (Workbook workbook = new XSSFWorkbook()) {
      Sheet sheet = workbook.createSheet("Transações");

      Row headerRow = sheet.createRow(0);
      headerRow.createCell(0).setCellValue("ID");
      headerRow.createCell(1).setCellValue("CPF");
      headerRow.createCell(2).setCellValue("Tipo");
      headerRow.createCell(3).setCellValue("Categoria");
      headerRow.createCell(4).setCellValue("Valor (Original)");
      headerRow.createCell(5).setCellValue("Moeda");
      headerRow.createCell(6).setCellValue("Valor (R$)");
      headerRow.createCell(7).setCellValue("Status");
      headerRow.createCell(8).setCellValue("Data");
      headerRow.createCell(9).setCellValue("Apenas Registro");

      int rowIdx = 1;
      for (Transacao t : lista) {
        Row row = sheet.createRow(rowIdx++);
        row.createCell(0).setCellValue(t.getId());
        row.createCell(1).setCellValue(t.getUsuarioId());
        row.createCell(2).setCellValue(t.getTipo() != null ? t.getTipo().name() : "");
        row.createCell(3).setCellValue(t.getCategoria() != null ? t.getCategoria().name() : "");
        row.createCell(4).setCellValue(t.getValorOriginal() != null ? t.getValorOriginal().doubleValue() : 0.0);
        row.createCell(5).setCellValue(t.getMoeda() != null ? t.getMoeda() : "");
        row.createCell(6).setCellValue(t.getValorBrl() != null ? t.getValorBrl().doubleValue() : 0.0);
        row.createCell(7).setCellValue(t.getStatus() != null ? t.getStatus().name() : "DESCONHECIDO");
        row.createCell(8).setCellValue(t.getDataCriacao() != null ? t.getDataCriacao().toString() : "");
        row.createCell(9).setCellValue(t.isApenasRegistro() ? "SIM" : "NÃO");
      }
      workbook.write(response.getOutputStream());
    }
  }

  @Operation(summary = "Solicitar nova transação", description = "Envia uma transação para processamento assíncrono via Kafka")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Transação criada com sucesso"),
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
  })
  @PostMapping
  public ResponseEntity<Transacao> solicitarTransacao(@RequestBody @Valid TransacaoRequestDto request) {
    Transacao novaTransacao = criarTransacao.executar(
      request.cpf(),
      request.valor(),
      request.moeda(),
      request.tipo(),
      request.categoria(),
      request.apenasRegistro()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(novaTransacao);
  }
}
