package ch.verno.gateway.endpoints.v1.pub.report;

import ch.verno.common.lib.api.ApiQueryParam;
import ch.verno.common.lib.api.ApiUrl;
import ch.verno.contract.endpoint.file.TempFileResource;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.TEMP_FILE_REPORT_PUBLIC)
public class ReportController {

  @Nonnull private final TempFileResource tempFileResource;

  public ReportController(@Nonnull final RpcFactory rpcFactory) {
    this.tempFileResource = rpcFactory.create(TempFileResource.class);
  }

  @GetMapping(value = "/{token}", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<ByteArrayResource> get(@PathVariable final String token,
                                               @RequestParam(defaultValue = ApiQueryParam.DISPOSITION_INLINE) final String disposition) {
    final var reportDto = tempFileResource.loadFile(token);

    final var contentDisposition = ContentDisposition
            .builder(ApiQueryParam.DISPOSITION_ATTACHMENT.equalsIgnoreCase(disposition) ?
                    ApiQueryParam.DISPOSITION_ATTACHMENT :
                    ApiQueryParam.DISPOSITION_INLINE)
            .filename(reportDto.filename())
            .build();

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
            .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
            .header(HttpHeaders.PRAGMA, "no-cache")
            .header(HttpHeaders.EXPIRES, "0")
            .contentLength(reportDto.pdfBytes().length)
            .body(new ByteArrayResource(reportDto.pdfBytes()));
  }
}