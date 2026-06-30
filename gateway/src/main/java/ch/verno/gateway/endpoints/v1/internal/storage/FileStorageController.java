package ch.verno.gateway.endpoints.v1.internal.storage;

import ch.verno.contract.dto.file.storage.api.DownloadFileResponse;
import ch.verno.contract.dto.file.storage.api.FileMetaResponse;
import ch.verno.contract.dto.file.storage.api.FileUploadResponse;
import ch.verno.contract.dto.table.file.FileUploadDto;
import ch.verno.contract.endpoint.file.StorageResource;
import ch.verno.contract.gateway.ApiUrl;
import ch.verno.lib.exception.file.FileUploadException;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(ApiUrl.FILES)
public class FileStorageController {

  @Nonnull private final StorageResource storageResource;

  public FileStorageController(@Nonnull final StorageResource storageResource) {
    this.storageResource = storageResource;
  }

  @PostMapping(consumes = "multipart/form-data")
  public FileUploadResponse upload(@RequestPart("file") MultipartFile file) {
    final var stored = storageResource.upload(toFileUploadDto(file));
    return new FileUploadResponse(
            stored.getId(),
            stored.getFilename(),
            stored.getContentType(),
            stored.getSize()
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<DownloadFileResponse> download(@PathVariable @Nonnull final Long id) {
    final var download = storageResource.download(id);

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
            meta.getFilename(),
            meta.getContentType(),
            meta.getSize(),
            meta.getChecksumSha256()
    ));
  }

  @GetMapping("/{id}/meta")
  public FileMetaResponse meta(@PathVariable @Nonnull final Long id) {
    final var meta = storageResource.getMeta(id);
    return new FileMetaResponse(
            meta.getId(),
            meta.getFilename(),
            meta.getContentType(),
            meta.getSize(),
            meta.getChecksumSha256()
    );
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable @Nonnull final Long id) {
    storageResource.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Nonnull
  private FileUploadDto toFileUploadDto(@Nonnull final MultipartFile file) throws FileUploadException {
    try {
      return new FileUploadDto(
              file.getOriginalFilename(),
              file.getContentType(),
              file.getInputStream(),
              file.getSize()
      );
    } catch (IOException e) {
      throw new FileUploadException("Failed to read uploaded file", e);
    }
  }
}