package ch.verno.gateway.endpoints.v1.internal.temp;

import ch.verno.contract.endpoint.file.TempFileResource;
import jakarta.annotation.Nonnull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class TempFileBaseController {

  @Nonnull private final TempFileResource tempFileResource;

  public TempFileBaseController(@Nonnull final TempFileResource tempFileResource) {
    this.tempFileResource = tempFileResource;
  }

  @Nonnull
  abstract ResponseEntity<ByteArrayResource> get(@PathVariable String token,
                                                 @RequestParam(defaultValue = "inline") String disposition);

  @Nonnull
  protected ResponseEntity<ByteArrayResource> getByToken(@Nonnull final String token,
                                                         @Nonnull final String disposition) {
    final var reportDto = tempFileResource.loadFile(token);

    final var cd = ContentDisposition
            .builder("attachment".equalsIgnoreCase(disposition) ? "attachment" : "inline")
            .filename(reportDto.filename())
            .build();

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, cd.toString())
            .header(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8")
            .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
            .header(HttpHeaders.PRAGMA, "no-cache")
            .header(HttpHeaders.EXPIRES, "0")
            .contentLength(reportDto.pdfBytes().length)
            .body(new ByteArrayResource(reportDto.pdfBytes()));
  }
}
