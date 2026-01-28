package com.br.financialmanager.infra.controller;

import com.br.financialmanager.application.usecases.transaction.BuscarTransacao;
import com.br.financialmanager.application.usecases.transaction.CriarTransacao;
import com.br.financialmanager.application.usecases.transaction.GerarAnaliseFinanceira;
import com.br.financialmanager.application.usecases.transaction.ListarTransacoes;
import com.br.financialmanager.domain.transaction.ResumoDiario;
import com.br.financialmanager.domain.transaction.Transacao;
import com.br.financialmanager.infra.controller.dto.TransacaoRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

  private final CriarTransacao criarTransacao;
  private final ListarTransacoes listarTransacoes;
  private final BuscarTransacao buscarTransacao;
  private final GerarAnaliseFinanceira gerarAnalise;

  public TransacaoController(CriarTransacao criarTransacao,
                             ListarTransacoes listarTransacoes,
                             BuscarTransacao buscarTransacao,
                             GerarAnaliseFinanceira gerarAnalise) {
    this.criarTransacao = criarTransacao;
    this.listarTransacoes = listarTransacoes;
    this.buscarTransacao = buscarTransacao;
    this.gerarAnalise = gerarAnalise;
  }

  @GetMapping
  public List<Transacao> listarTodas() {
    return listarTransacoes.obterTodasTransacoes();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Transacao> buscarPorId(@PathVariable String id) {
    Transacao transacao = buscarTransacao.buscarPorId(id);
    if (transacao != null) {
      return ResponseEntity.ok(transacao);
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/analise")
  public List<ResumoDiario> analiseMensal(@RequestParam int mes, @RequestParam int ano) {
    return gerarAnalise.executar(mes, ano);
  }

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
      headerRow.createCell(2).setCellValue("Tipo"); // <--- NOVA COLUNA
      headerRow.createCell(3).setCellValue("Valor (Original)");
      headerRow.createCell(4).setCellValue("Moeda");
      headerRow.createCell(5).setCellValue("Valor (R$)");
      headerRow.createCell(6).setCellValue("Status");
      headerRow.createCell(7).setCellValue("Data");

      int rowIdx = 1;
      for (Transacao t : lista) {
        Row row = sheet.createRow(rowIdx++);

        row.createCell(0).setCellValue(t.getId());
        row.createCell(1).setCellValue(t.getUsuarioId());

        row.createCell(2).setCellValue(t.getTipo() != null ? t.getTipo().name() : "");

        if (t.getValorOriginal() != null) {
          row.createCell(3).setCellValue(t.getValorOriginal().doubleValue());
        } else {
          row.createCell(3).setCellValue(0.0);
        }

        row.createCell(4).setCellValue(t.getMoeda() != null ? t.getMoeda() : "");

        if (t.getValorBrl() != null) {
          row.createCell(5).setCellValue(t.getValorBrl().doubleValue());
        } else {
          BigDecimal valorAntigo = t.getValorOriginal() != null ? t.getValorOriginal() : BigDecimal.ZERO;
          row.createCell(5).setCellValue(valorAntigo.doubleValue());
        }

        row.createCell(6).setCellValue(t.getStatus() != null ? t.getStatus().name() : "DESCONHECIDO");
        row.createCell(7).setCellValue(t.getDataCriacao() != null ? t.getDataCriacao().toString() : "");
      }

      workbook.write(response.getOutputStream());
    }
  }

  @PostMapping
  public ResponseEntity<Transacao> solicitarTransacao(@RequestBody @Valid TransacaoRequestDto request) {

    Transacao novaTransacao = criarTransacao.executar(
      request.cpf(),
      request.valor(),
      request.moeda(),
      request.tipo()
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(novaTransacao);
  }
}
