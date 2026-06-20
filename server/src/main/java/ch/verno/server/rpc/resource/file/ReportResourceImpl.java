package ch.verno.server.rpc.resource.file;

import ch.verno.contract.endpoint.file.ReportResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;

@RpcResource(ReportResource.class)
public class ReportResourceImpl implements ReportResource {

  public ReportResourceImpl(@Nonnull final ServerBean serverBean){

  }

}
