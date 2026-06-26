package ch.verno.server.service.base;

import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface IEntityServiceExtendedById<DTO, ID> extends IEntityService<DTO> {

  @Nonnull
  Optional<DTO> findById(@Nonnull ID id);

  boolean deleteById(@Nonnull ID id);

}
