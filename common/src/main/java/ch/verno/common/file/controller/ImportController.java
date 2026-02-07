package ch.verno.common.file.controller;

import ch.verno.common.file.FileServerGate;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.ApiUrl;
import jakarta.annotation.Nonnull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.TEMP_FILE_IMPORT)
public class ImportController extends BaseController {

  public ImportController(@Nonnull GlobalInterface globalInterface) {
    super(globalInterface.getGate(FileServerGate.class));
  }

  @Nonnull
  @Override
  @GetMapping(value = "/{token}", produces = "text/csv")
  public ResponseEntity<ByteArrayResource> get(@PathVariable String token,
                                               @RequestParam(defaultValue = "inline") String disposition) {
    return getByToken(token, disposition);
  }
}
