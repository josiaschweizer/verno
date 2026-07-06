package ch.verno.server.rpc.resource.io;

import ch.verno.common.server.io.importing.CsvSchema;
import ch.verno.contract.endpoint.io.ImportResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.file.StorageBo;
import ch.verno.server.io.importing.SchemaResolver;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
@RpcResource(ImportResource.class)
public class ImportResourceImpl implements ImportResource {

  public ImportResourceImpl(@Nonnull final ServerBean serverBean) {

  }

  @Nonnull
  @Override
  public CsvSchema resolveCsvSchema(@Nonnull final String fileToken) {
    return null;
  }
}
