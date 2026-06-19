package ch.verno.rpc.client.gender;

import ch.verno.contract.endpoint.gender.GenderResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

public class GenderClient {

  @Nonnull private final Lazy<GenderResource> genderResource;

  @Inject
  public GenderClient(@Nonnull final RpcFactory rpcFactory) {
    this.genderResource = Lazy.of(() -> rpcFactory.create(GenderResource.class));
  }

}
