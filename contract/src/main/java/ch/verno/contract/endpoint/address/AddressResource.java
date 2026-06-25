package ch.verno.contract.endpoint.address;

import ch.verno.contract.dto.table.address.AddressDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

@RpcEndpoint
public interface AddressResource {

  @Nonnull
  AddressDto findOrCreateAddress(@Nonnull AddressDto address);

}
