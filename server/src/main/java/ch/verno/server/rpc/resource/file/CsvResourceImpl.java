package ch.verno.server.rpc.resource.file;

import ch.verno.contract.endpoint.file.CsvResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;

@RpcResource(CsvResource.class)
public class CsvResourceImpl implements CsvResource {

  public CsvResourceImpl(@Nonnull final ServerBean serverBean) {

  }

}
