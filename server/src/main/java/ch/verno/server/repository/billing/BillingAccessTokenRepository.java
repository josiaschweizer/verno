package ch.verno.server.repository.billing;

import ch.verno.db.entity.billing.BillingAccessTokenEntity;
import ch.verno.db.jpa.billing.SpringDataBillingAccessTokenJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class BillingAccessTokenRepository extends AbstractEntityRepository<
        BillingAccessTokenEntity,
        Long,
        SpringDataBillingAccessTokenJpaRepository> {

  public BillingAccessTokenRepository(@Nonnull final SpringDataBillingAccessTokenJpaRepository repository) {
    super(repository);
  }

  @Nonnull
  public Optional<BillingAccessTokenEntity> findByTokenHash(@Nonnull final String tokenHash) {
    return getRepository().findByTokenHash(tokenHash);
  }

  public boolean existsByTokenHash(@Nonnull final String tokenHash) {
    return getRepository().existsByTokenHash(tokenHash);
  }

}