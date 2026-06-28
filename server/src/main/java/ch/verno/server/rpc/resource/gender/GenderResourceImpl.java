package ch.verno.server.rpc.resource.gender;

import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.contract.endpoint.gender.GenderResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.table.gender.GenderBo;
import ch.verno.server.service.intern.table.gender.GenderService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RpcResource(GenderResource.class)
public class GenderResourceImpl implements GenderResource {

  @Nonnull private final Lazy<GenderBo> genderBo;

  public GenderResourceImpl(@Nonnull final ServerBean serverBean) {
    this.genderBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(GenderBo.class));
  }

  @Nonnull
  @Override
  public Optional<GenderDto> getGenderByName(@Nonnull final String name) {
    return genderBo.get().findByName(name);
  }

  @Nonnull
  @Override
  public List<GenderDto> getAllGenders() {
    return genderBo.get().findAll();
  }

  @Nonnull
  @Override
  public GenderDto saveGender(@Nonnull final GenderDto gender) {
    return genderBo.get().save(gender);
  }
}
