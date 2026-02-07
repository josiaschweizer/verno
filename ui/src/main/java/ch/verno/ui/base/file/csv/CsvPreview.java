package ch.verno.ui.base.file.csv;

import ch.verno.common.exceptions.io.ParseCsvException;
import ch.verno.common.file.FileServerGate;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.Publ;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVFormat;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CsvPreview extends VerticalLayout {

  public CsvPreview(@Nonnull final String fileUrl) {
    setPadding(false);
    setSpacing(false);
    setSizeFull();

    final var token = extractToken(fileUrl);
    final var csvData = loadCsvData(token);

    if (!csvData.isEmpty()) {
      final var grid = createGrid(csvData);
      add(grid);
    } else {
      add(new Div(getTranslation("shared.keine.daten.verfugbar")));
    }
  }

  @Nonnull
  private String extractToken(@Nonnull final String url) {
    final var parts = url.split(Publ.SLASH);
    final var lastPart = parts[parts.length - 1];
    return lastPart.split("\\?")[0];
  }

  @Nonnull
  private List<Map<String, String>> loadCsvData(@Nonnull final String token) {
    try {
      final var globalGate = GlobalInterface.getInstance();
      final var fileServerGate = globalGate.getGate(FileServerGate.class);
      final var fileDto = fileServerGate.loadFile(token);

      return parseCsv(fileDto.pdfBytes());
    } catch (Exception e) {
      throw new ParseCsvException(e.getMessage(), e);
    }
  }

  @Nonnull
  private List<Map<String, String>> parseCsv(@Nonnull final byte[] csvBytes) {
    try (final var reader = new InputStreamReader(new ByteArrayInputStream(csvBytes), StandardCharsets.UTF_8);
         final var csvParser = CSVFormat.DEFAULT.builder()
                 .setHeader()
                 .setSkipHeaderRecord(true)
                 .setTrim(true)
                 .build()
                 .parse(reader)) {

      final var headers = csvParser.getHeaderNames();
      final var data = new ArrayList<Map<String, String>>();

      for (final var record : csvParser) {
        final var row = new LinkedHashMap<String, String>();

        for (final var header : headers) {
          row.put(header, record.isMapped(header) ? record.get(header) : Publ.EMPTY_STRING);
        }

        if (!row.isEmpty()) {
          data.add(row);
        }
      }

      return data;
    } catch (Exception e) {
      throw new ParseCsvException(e.getMessage(), e);
    }
  }

  @Nonnull
  private Grid<Map<String, String>> createGrid(@Nonnull final List<Map<String, String>> data) {
    final var grid = new Grid<Map<String, String>>();
    grid.setSizeFull();

    if (!data.isEmpty()) {
      final var firstRow = data.getFirst();
      for (String columnName : firstRow.keySet()) {
        grid.addColumn(row -> row.getOrDefault(columnName, ""))
                .setHeader(columnName)
                .setAutoWidth(true)
                .setFlexGrow(1);
      }
    }

    grid.setItems(data);

    return grid;
  }
}
