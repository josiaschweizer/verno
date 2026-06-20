package ch.verno.rpc.client.file;

import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

public class ReportClient {

  @Nonnull private final Lazy<ReportClient> reportClient;

  @Inject
  public ReportClient(@Nonnull final RpcFactory rpcFactory) {
    this.reportClient = Lazy.of(() -> rpcFactory.create(ReportClient.class));
  }

}
