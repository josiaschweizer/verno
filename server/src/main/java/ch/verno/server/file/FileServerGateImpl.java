package ch.verno.server.file;

import ch.verno.common.file.dto.CsvMapDto;
import ch.verno.common.file.dto.FileDto;
import ch.verno.common.file.FileServerGate;
import ch.verno.server.io.importing.csv.CsvImportUtil;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServerGateImpl implements FileServerGate {

  @Nonnull private final FileStorageHandler fileStorageHandler;

  public FileServerGateImpl() {
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

  @NonNull
  @Override
  public List<CsvMapDto> parseRows(@NonNull final FileDto fileDto) {
    return CsvImportUtil.parseRows(fileDto.pdfBytes());
  }

  @NonNull
  @Override
  public FileDto parseRows(@NonNull final List<CsvMapDto> rows, @NonNull final String fileName) {
    return new FileDto(fileName, CsvImportUtil.createFileDtoFromRows(rows));
  }

  @Override
  public void delete(@Nonnull final String token) {
    fileStorageHandler.delete(token);
  }
}
