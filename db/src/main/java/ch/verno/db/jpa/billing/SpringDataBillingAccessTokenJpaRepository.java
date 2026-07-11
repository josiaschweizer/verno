package ch.verno.db.jpa.billing;

import ch.verno.db.entity.billing.BillingAccessTokenEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface SpringDataBillingAccessTokenJpaRepository extends AbstractEntityJpaRepository<BillingAccessTokenEntity, Long> {

  @Nonnull
  Optional<BillingAccessTokenEntity> findByTokenHash(@Nonnull String tokenHash);

  boolean existsByTokenHash(@Nonnull String tokenHash);
}
