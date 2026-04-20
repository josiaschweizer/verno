package ch.verno.server.file;

import ch.verno.common.api.dto.internal.file.temp.CsvMapDto;
import ch.verno.common.api.dto.internal.file.temp.FileDto;
import ch.verno.common.gate.server.TempFileServerGate;
import ch.verno.common.server.io.importing.CsvDelimiter;
import ch.verno.server.io.importing.csv.CsvImportUtil;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TempTempFileServerGateImpl implements TempFileServerGate {

  @Nonnull private final FileStorageHandler fileStorageHandler;

  public TempTempFileServerGateImpl() {
    fileStorageHandler = new FileStorageHandler();
  }

  @Nonnull
  @Override
  public String store(final @NonNull String filename, final byte @NonNull [] fileBytes) {
    return fileStorageHandler.storeFileTemporary(filename, fileBytes);
  }

  @Nonnull
  @Override
  public String store(@Nonnull final FileDto file) {
    return fileStorageHandler.storeFileTemporary(file.filename(), file.pdfBytes());
  }

  @Nonnull
  @Override
  public FileDto loadFile(@Nonnull final String token) {
    return fileStorageHandler.getFileByToken(token);
  }

  @Nonnull
  @Override
  public List<CsvMapDto> parseRows(@Nonnull final FileDto fileDto) {
    return CsvImportUtil.parseRows(fileDto.pdfBytes());
  }

  @Nonnull
  @Override
  public List<CsvMapDto> parseRows(@Nonnull final FileDto fileDto, @Nonnull final CsvDelimiter delimiter) {
    return CsvImportUtil.parseRows(fileDto.pdfBytes(), delimiter);
  }

  @Nonnull
  @Override
  public FileDto parseRows(@Nonnull final List<CsvMapDto> rows, @Nonnull final String fileName) {
    return new FileDto(fileName, CsvImportUtil.createFileDtoFromRows(rows));
  }

  @Override
  public void delete(@Nonnull final String token) {
    fileStorageHandler.delete(token);
  }
}
