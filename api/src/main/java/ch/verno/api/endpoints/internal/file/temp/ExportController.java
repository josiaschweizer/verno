package ch.verno.api.endpoints.internal.file.temp;

import ch.verno.common.gate.servergate.TempFileServerGate;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.ApiUrl;
import jakarta.annotation.Nonnull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.TEMP_FILE_EXPORT)
public class ExportController extends TempFileBaseController {

  public ExportController(@Nonnull final GlobalInterface globalInterface) {
    super(globalInterface.getGate(TempFileServerGate.class));
  }

  @Nonnull
  @Override
  @GetMapping(value = "/{token}", produces = "text/csv")
  public ResponseEntity<ByteArrayResource> get(@PathVariable String token,
                                               @RequestParam(defaultValue = "inline") String disposition) {
    return getByToken(token, disposition);
  }

}
