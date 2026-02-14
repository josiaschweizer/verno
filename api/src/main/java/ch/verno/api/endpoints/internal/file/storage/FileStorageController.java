package ch.verno.api.endpoints.internal.file.storage;

import ch.verno.common.api.dto.internal.file.storage.FileMetaResponse;
import ch.verno.common.api.dto.internal.file.storage.FileUploadResponse;
import ch.verno.common.db.dto.file.StoredFile;
import ch.verno.publ.ApiUrl;
import ch.verno.publ.Publ;
import ch.verno.server.service.intern.FileStorageService;
import jakarta.annotation.Nonnull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
  public ResponseEntity<StreamingResponseBody> download(@PathVariable @Nonnull final Long id) {
    final var download = service.download(id);

    final var encoded = URLEncoder.encode(download.meta().filename(), StandardCharsets.UTF_8).replace(Publ.PLUS, "%20");
    final var contentDisposition = "attachment; filename=\"" + download.meta().filename().replace("\"", "") + "\"; filename*=UTF-8''" + encoded;

    StreamingResponseBody body = out -> {
      try (var in = download.stream()) {
        in.transferTo(out);
      }
    };

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
            .header(HttpHeaders.CONTENT_TYPE, download.meta().contentType())
            .header("X-Checksum-SHA256", download.meta().checksumSha256())
            .contentLength(download.meta().size())
            .body(body);
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