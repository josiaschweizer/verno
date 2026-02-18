package ch.verno.api.endpoints.internal.file.storage;

import ch.verno.common.api.dto.internal.file.storage.DownloadFileResponse;
import ch.verno.common.api.dto.internal.file.storage.FileMetaResponse;
import ch.verno.common.api.dto.internal.file.storage.FileUploadResponse;
import ch.verno.common.db.dto.file.StoredFile;
import ch.verno.publ.ApiUrl;
import ch.verno.server.service.intern.FileStorageService;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(ApiUrl.FILES)
public class FileStorageController {

  @Nonnull private final FileStorageService service;

  public FileStorageController(@Nonnull final FileStorageService service) {
    this.service = service;
  }

  @PostMapping(consumes = "multipart/form-data")
  public FileUploadResponse upload(@RequestPart("file") MultipartFile file) {
    final var stored = service.upload(file);
    return new FileUploadResponse(stored.id(), stored.filename(), stored.contentType(), stored.size());
  }

  @GetMapping("/{id}")
  public ResponseEntity<DownloadFileResponse> downloadDto(@PathVariable @Nonnull final Long id) {
    final var download = service.download(id);

    final byte[] bytes;
    try (final var inputStream = download.stream()) {
      if (inputStream != null) {
        bytes = inputStream.readAllBytes();
      } else {
        bytes = new byte[0];
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to read file stream", e);
    }

    final var meta = download.meta();
    return ResponseEntity.ok(new DownloadFileResponse(
            bytes,
            meta.filename(),
            meta.contentType(),
            meta.size(),
            meta.checksumSha256()
    ));
  }

  @GetMapping("/{id}/meta")
  public FileMetaResponse meta(@PathVariable @Nonnull final Long id) {
    StoredFile meta = service.getMeta(id);
    return new FileMetaResponse(meta.id(), meta.filename(), meta.contentType(), meta.size(), meta.checksumSha256());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable @Nonnull final Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}