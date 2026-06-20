package ch.verno.rpc.client.gender;

import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.contract.endpoint.gender.GenderResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("UnusedReturnValue")
public class GenderClient {

  @Nonnull private final Lazy<GenderResource> genderResource;

  @Inject
  public GenderClient(@Nonnull final RpcFactory rpcFactory) {
    this.genderResource = Lazy.of(() -> rpcFactory.create(GenderResource.class));
  }

  @Nonnull
  public Optional<GenderDto> getGenderByName(@Nonnull final String name) {
    return genderResource.get().getGenderByName(name);
  }

  @Nonnull
  public List<GenderDto> getAllGenders() {
    return genderResource.get().getAllGenders();
  }

  @Nonnull
  public GenderDto saveGender(@Nonnull final GenderDto gender) {
    return genderResource.get().saveGender(gender);
  }
}
