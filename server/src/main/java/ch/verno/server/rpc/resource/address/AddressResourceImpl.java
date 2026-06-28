package ch.verno.server.rpc.resource.address;

import ch.verno.contract.dto.table.address.AddressDto;
import ch.verno.contract.endpoint.address.AddressResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.table.address.AddressBo;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
@RpcResource(AddressResource.class)
public class AddressResourceImpl implements AddressResource {

  @Nonnull private final Lazy<AddressBo> addressBo;

  public AddressResourceImpl(@Nonnull final ServerBean serverBean) {
    this.addressBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(AddressBo.class));
  }

  @Nonnull
  @Override
  public AddressDto findOrCreateAddress(@Nonnull final AddressDto address) {
    return addressBo.get().findOrCreate(address);
  }
}
