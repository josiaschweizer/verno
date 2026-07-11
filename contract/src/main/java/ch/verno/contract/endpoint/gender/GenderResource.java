package ch.verno.contract.endpoint.gender;

import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

@RpcEndpoint
public interface GenderResource {

  @Nonnull
  Optional<GenderDto> getGenderByName(@Nonnull String name);

  @Nonnull
  List<GenderDto> getAllGenders();

  @Nonnull
  GenderDto saveGender(@Nonnull  GenderDto gender);

}