package ch.verno.gateway.endpoints.v1.internal.temp;

import ch.verno.contract.endpoint.file.TempFileResource;
import ch.verno.common.lib.api.ApiUrl;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.TEMP_FILE_EXPORT)
public class ExportController extends TempFileBaseController {

  public ExportController(@Nonnull final RpcFactory rpcFactory) {
    super( rpcFactory.create(TempFileResource.class));
  }

  @Nonnull
  @Override
  @GetMapping(value = "/{token}", produces = "text/csv")
  public ResponseEntity<ByteArrayResource> get(@PathVariable String token,
                                               @RequestParam(defaultValue = "inline") String disposition) {
    return getByToken(token, disposition);
  }

}
