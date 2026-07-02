package ch.verno.gateway.endpoints.v1.pub.auth.report;

import ch.verno.common.lib.api.ApiQueryParam;
import ch.verno.common.lib.api.ApiUrl;
import ch.verno.common.lib.http.HttpConstants;
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
@RequestMapping(ApiUrl.TEMP_FILE_REPORT_PUBLIC_AUTH)
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
            .header(HttpHeaders.CACHE_CONTROL, HttpConstants.NO_CACHE_NO_STORE_MUST_REVALIDATE)
            .header(HttpHeaders.PRAGMA, HttpConstants.NO_CACHE)
            .header(HttpHeaders.EXPIRES, HttpConstants.EXPIRES_IMMEDIATELY)
            .contentLength(reportDto.pdfBytes().length)
            .body(new ByteArrayResource(reportDto.pdfBytes()));
  }
}