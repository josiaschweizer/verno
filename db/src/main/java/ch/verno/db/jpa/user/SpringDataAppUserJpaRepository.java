package ch.verno.db.jpa.user;

import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface SpringDataAppUserJpaRepository extends AbstractEntityJpaRepository<AppUserEntity, Long> {

  @Nonnull
  Optional<AppUserEntity> findByUsername(@Nonnull final String username);

  Optional<AppUserEntity> findByEmail(@Nonnull final String email);

  boolean existsByUsernameAndTenantId(@Nonnull String username,
                                      @Nonnull Long tenantId);
}
