package ch.verno.common.db.service;

import ch.verno.common.db.dto.file.FileDownload;
import ch.verno.common.db.dto.file.StoredFile;
import jakarta.annotation.Nonnull;
import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {

  @Nonnull
  StoredFile upload(@Nonnull MultipartFile file);

  @Nonnull
  FileDownload download(@Nonnull Long id);

  @Nonnull
  StoredFile getMeta(@Nonnull Long id);

  void delete(@Nonnull Long id);
}
