package ch.verno.db.jpa.billing;

import ch.verno.db.entity.billing.BillingAccessTokenEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataBillingAccessTokenJpaRepository extends
        JpaRepository<BillingAccessTokenEntity, Long> {

  @Nonnull
  Optional<BillingAccessTokenEntity> findByTokenHash(@Nonnull String tokenHash);

  boolean existsByTokenHash(@Nonnull String tokenHash);
}
