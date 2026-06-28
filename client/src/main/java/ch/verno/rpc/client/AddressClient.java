package ch.verno.rpc.client;

import ch.verno.contract.dto.table.address.AddressDto;
import ch.verno.contract.endpoint.address.AddressResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

public class AddressClient {

  @Nonnull private final Lazy<AddressResource> addressResource;

  @Inject
  public AddressClient(@Nonnull final RpcFactory rpcFactory) {
    this.addressResource = Lazy.of(() -> rpcFactory.create(AddressResource.class));
  }

  @Nonnull
  public AddressDto findOrCreateAddress(@Nonnull final AddressDto address){
    return addressResource.get().findOrCreateAddress(address);
  }

}
