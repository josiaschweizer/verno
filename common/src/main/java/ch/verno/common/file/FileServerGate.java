package ch.verno.common.file;

import ch.verno.common.file.dto.CsvMapDto;
import ch.verno.common.file.dto.FileDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface FileServerGate {

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

  String generateFile(List<CsvMapDto> rows);
}
