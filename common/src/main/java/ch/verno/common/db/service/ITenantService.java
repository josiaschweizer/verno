package ch.verno.common.db.service;

import ch.verno.common.db.dto.table.TenantDto;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface ITenantService {

  @Nonnull
  Optional<Long> findTenantIdBySlug(@Nonnull String slug);

  @Nonnull
  Optional<TenantDto> findById(@Nonnull Long id);

  @Nonnull
  List<TenantDto> findAllTenants();
}
