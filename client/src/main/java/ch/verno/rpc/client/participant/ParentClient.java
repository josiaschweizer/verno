package ch.verno.rpc.client.participant;

import ch.verno.contract.dto.table.participant.ParentDto;
import ch.verno.contract.endpoint.participant.ParentResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

public class ParentClient {

  @Nonnull private final Lazy<ParentResource> parentResource;

  @Inject
  public ParentClient(@Nonnull final RpcFactory rpcFactory){
    this.parentResource = Lazy.of(() -> rpcFactory.create(ParentResource.class));
  }

  @Nonnull
  public ParentDto findOrCreateParent(@Nonnull final ParentDto parent) {
    return parentResource.get().findOrCreateParent(parent);
  }

  public void deleteParentById(@Nonnull final Long id) {
    parentResource.get().deleteParentById(id);
  }

}
