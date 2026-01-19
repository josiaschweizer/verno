package ch.verno.common.file;

import ch.verno.common.gate.GlobalGate;
import ch.verno.publ.ApiUrl;
import jakarta.annotation.Nonnull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.TEMP_FILE_IMPORT)
public class FileController {

  @Nonnull private final FileServerGate fileServerGate;

  public FileController(@Nonnull GlobalGate globalGate) {
    fileServerGate = globalGate.getGate(FileServerGate.class);
  }

  @GetMapping(value = "/{token}", produces = "text/csv")
  public ResponseEntity<ByteArrayResource> get(@PathVariable String token,
                                               @RequestParam(defaultValue = "inline") String disposition) {
    final var reportDto = fileServerGate.loadFile(token);

    ContentDisposition cd = ContentDisposition
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
