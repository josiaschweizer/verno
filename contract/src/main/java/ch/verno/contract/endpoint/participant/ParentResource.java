package ch.verno.contract.endpoint.participant;

import ch.verno.contract.dto.table.participant.ParentDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

@RpcEndpoint
public interface ParentResource {

  @Nonnull
  ParentDto findOrCreateParent(@Nonnull final ParentDto parent);

  void deleteParentById(@Nonnull Long id);

}
