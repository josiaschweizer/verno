package ch.verno.common.server.service.intern.tenant;

import ch.verno.common.db.dto.table.TenantDto;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface ITenantService {

  @Nonnull
  Optional<Long> findTenantIdBySlug(@Nonnull String slug);

  @Nonnull
  Optional<Long> findTenantIdByName(@Nonnull String name);

  @Nonnull
  Optional<TenantDto> findById(@Nonnull Long id);

  @Nonnull
  List<TenantDto> findAllTenants();
}
