package ch.verno.common.gate.servergate;

import ch.verno.common.api.dto.internal.file.temp.CsvMapDto;
import ch.verno.common.api.dto.internal.file.temp.FileDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface TempFileServerGate {

  @Nonnull
  String store(@Nonnull String filename, @Nonnull byte[] fileBytes);

  String store(@Nonnull final FileDto file);

  @Nonnull
  FileDto loadFile(@Nonnull String token);

  @Nonnull
  List<CsvMapDto> parseRows(@Nonnull final FileDto fileDto);

  @Nonnull
  FileDto parseRows(@Nonnull final List<CsvMapDto> rows, @Nonnull final String fileName);

  void delete(@Nonnull String token);
}
