package com.br.financialmanager.infra.gateways.user;

import com.br.financialmanager.application.gateways.user.LeitorDeArquivo;
import com.br.financialmanager.domain.entities.Usuario;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LeitorDeExcel implements LeitorDeArquivo {

  private static final Logger log = LoggerFactory.getLogger(LeitorDeExcel.class);

  @Override
  public List<Usuario> lerUsuarios(InputStream arquivo) {
    List<Usuario> usuarios = new ArrayList<>();
    DataFormatter formatter = new DataFormatter();

    try (Workbook workbook = WorkbookFactory.create(arquivo)) {
      Sheet sheet = workbook.getSheetAt(0);

      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) continue;

        try {
          String nome = formatter.formatCellValue(row.getCell(0));
          String cpf = formatter.formatCellValue(row.getCell(1));
          String dataString = formatter.formatCellValue(row.getCell(2));
          String email = formatter.formatCellValue(row.getCell(3));
          String senha = formatter.formatCellValue(row.getCell(4));

          if (nome.isEmpty() || cpf.isEmpty()) continue;

          LocalDate nascimento;
          try {
            if (dataString.contains("/")) {
              nascimento = LocalDate.parse(dataString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else {
              nascimento = LocalDate.parse(dataString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
          } catch (Exception e) {
            log.warn("⚠️ Data inválida na linha {}: {}", (i + 1), dataString);
            nascimento = LocalDate.now();
          }

          usuarios.add(new Usuario(cpf, nome, nascimento, email, senha));

        } catch (Exception e) {
          log.error("❌ Erro na linha {}: {}", (i + 1), e.getMessage());
        }
      }
    } catch (Exception e) {
      log.error("Erro fatal ao processar Excel: {}", e.getMessage(), e);
      throw new RuntimeException("Erro ao processar Excel: " + e.getMessage(), e);
    }
    return usuarios;
  }
}
