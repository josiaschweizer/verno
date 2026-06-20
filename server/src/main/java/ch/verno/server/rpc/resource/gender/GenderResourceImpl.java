package ch.verno.server.rpc.resource.gender;

import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.contract.endpoint.gender.GenderResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.intern.table.gender.GenderService;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

@RpcResource(GenderResource.class)
public class GenderResourceImpl implements GenderResource {

  @Nonnull private final Lazy<GenderService> genderService;

  public GenderResourceImpl(@Nonnull final ServerBean serverBean) {
    this.genderService = Lazy.of(() -> new GenderService(serverBean));
  }

  @Nonnull
  @Override
  public Optional<GenderDto> getGenderByName(@Nonnull final String name) {
    return genderService.get().findByName(name);
  }

  @Nonnull
  @Override
  public List<GenderDto> getAllGenders() {
    return genderService.get().findAll();
  }

  @Nonnull
  @Override
  public GenderDto saveGender(@Nonnull final GenderDto gender) {
    return genderService.get().save(gender);
  }
}
