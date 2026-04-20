package ch.verno.common.gate.server;

import ch.verno.common.api.dto.internal.file.temp.CsvMapDto;
import ch.verno.common.api.dto.internal.file.temp.FileDto;
import ch.verno.common.server.io.importing.CsvDelimiter;
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
  List<CsvMapDto> parseRows(@Nonnull FileDto fileDto,
                            @Nonnull CsvDelimiter delimiter);


  @Nonnull
  FileDto parseRows(@Nonnull final List<CsvMapDto> rows, @Nonnull final String fileName);

  void delete(@Nonnull String token);
}
