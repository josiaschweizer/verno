package ch.verno.server.repository.billing;

import ch.verno.db.entity.billing.BillingAccessTokenEntity;
import ch.verno.db.jpa.billing.SpringDataBillingAccessTokenJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BillingAccessTokenRepository {

  @Nonnull
  private final SpringDataBillingAccessTokenJpaRepository jpaRepository;

  public BillingAccessTokenRepository(@Nonnull final SpringDataBillingAccessTokenJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  public BillingAccessTokenEntity save(@Nonnull final BillingAccessTokenEntity entity) {
    return jpaRepository.save(entity);
  }

  @Nonnull
  public Optional<BillingAccessTokenEntity> findById(@Nonnull final Long id) {
    return jpaRepository.findById(id);
  }

  @Nonnull
  public Optional<BillingAccessTokenEntity> findByTokenHash(@Nonnull final String tokenHash) {
    return jpaRepository.findByTokenHash(tokenHash);
  }

  public boolean existsByTokenHash(@Nonnull final String tokenHash) {
    return jpaRepository.existsByTokenHash(tokenHash);
  }

  public boolean existsById(@Nonnull final Long id) {
    return jpaRepository.existsById(id);
  }

  @Nonnull
  public List<BillingAccessTokenEntity> findAll() {
    return jpaRepository.findAll();
  }

  public void deleteById(@Nonnull final Long id) {
    jpaRepository.deleteById(id);
  }
}