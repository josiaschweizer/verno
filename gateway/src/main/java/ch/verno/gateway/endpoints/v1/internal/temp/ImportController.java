package ch.verno.gateway.endpoints.v1.internal.temp;

import ch.verno.contract.endpoint.file.TempFileResource;
import ch.verno.contract.gateway.ApiUrl;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.TEMP_FILE_IMPORT)
public class ImportController extends TempFileBaseController {

  public ImportController(@Nonnull RpcFactory rpcFactory) {
    super(rpcFactory.create(TempFileResource.class));
  }

  @Nonnull
  @Override
  @GetMapping(value = "/{token}", produces = "text/csv")
  public ResponseEntity<ByteArrayResource> get(@PathVariable String token,
                                               @RequestParam(defaultValue = "inline") String disposition) {
    return getByToken(token, disposition);
  }
}
