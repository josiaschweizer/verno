package ch.verno.server.rpc.resource.file;

import ch.verno.contract.endpoint.file.TempFileResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;

@RpcResource(TempFileResource.class)
public class TempFileResourceImpl implements TempFileResource {

  public TempFileResourceImpl(@Nonnull final ServerBean serverBean){

  }

}
