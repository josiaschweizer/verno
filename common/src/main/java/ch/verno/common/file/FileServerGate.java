package ch.verno.common.file;

import jakarta.annotation.Nonnull;

import java.util.List;

public interface FileServerGate {

  @Nonnull
  String store(String filename, byte[] fileBytes);

  @Nonnull
  FileDto loadFile(@Nonnull String token);

  @Nonnull
  List<CsvMapDto> parseRows(@Nonnull final FileDto fileDto);

  void delete(@Nonnull String token);
}
