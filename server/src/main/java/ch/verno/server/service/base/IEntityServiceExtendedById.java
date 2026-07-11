package ch.verno.server.service.base;

import ch.verno.contract.dto.response.base.delete.DeleteResponse;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface IEntityServiceExtendedById<DTO, ID> extends IEntityService<DTO> {

  boolean existsById(@Nonnull ID id);

  @Nonnull
  Optional<DTO> findById(@Nonnull ID id);

  @Nonnull
  DTO findByIdRequired(@Nonnull ID id);

  @Nonnull
  DeleteResponse deleteById(@Nonnull ID id);

}
