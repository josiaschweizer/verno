package ch.verno.server.file;

import ch.verno.common.file.CsvMapDto;
import ch.verno.common.file.FileDto;
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
  public String store(final String filename, final byte[] fileBytes) {
    return fileStorageHandler.storeFileTemporary(filename, fileBytes);
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

  @Override
  public void delete(@Nonnull final String token) {
    fileStorageHandler.delete(token);
  }
}
