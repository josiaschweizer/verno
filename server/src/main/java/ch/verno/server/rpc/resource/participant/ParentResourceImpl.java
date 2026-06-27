package ch.verno.server.rpc.resource.participant;

import ch.verno.contract.dto.table.participant.ParentDto;
import ch.verno.contract.endpoint.participant.ParentResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.table.participant.ParentBo;
import ch.verno.server.service.intern.table.participant.ParentService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
@RpcResource(ParentResource.class)
public class ParentResourceImpl implements ParentResource {

  @Nonnull private final Lazy<ParentBo> parentBo;
  @Nonnull private final Lazy<ParentService> parentService;

  public ParentResourceImpl(@Nonnull final ServerBean serverBean) {
    this.parentBo = Lazy.of(() -> serverBean.get(BoFactory.class).get(ParentBo.class));

    this.parentService = Lazy.of(() -> serverBean.get(ParentService.class));
  }

  @Nonnull
  @Override
  public ParentDto findOrCreateParent(@Nonnull final ParentDto parent) {
    return parentBo.get().findOrCreate(parent);
  }

  @Override
  public void deleteParentById(@Nonnull final Long id) {
    parentService.get().deleteById(id);
  }
}
