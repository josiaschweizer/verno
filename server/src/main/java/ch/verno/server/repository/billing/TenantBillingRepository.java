package ch.verno.server.repository.billing;

import ch.verno.db.entity.billing.TenantBillingEntity;
import ch.verno.db.jpa.billing.SpringDataTenantBillingJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TenantBillingRepository {

  @Nonnull
  private final SpringDataTenantBillingJpaRepository jpaRepository;

  public TenantBillingRepository(@Nonnull final SpringDataTenantBillingJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  public TenantBillingEntity save(@Nonnull final TenantBillingEntity entity) {
    return jpaRepository.save(entity);
  }

  @Nonnull
  public Optional<TenantBillingEntity> findById(@Nonnull final Long id) {
    return jpaRepository.findById(id);
  }

  @Nonnull
  public Optional<TenantBillingEntity> findByTenantId(@Nonnull final Long tenantId) {
    return jpaRepository.findByTenant_Id(tenantId);
  }

  public boolean existsByTenantId(@Nonnull final Long tenantId) {
    return jpaRepository.existsByTenant_Id(tenantId);
  }

  public boolean existsById(@Nonnull final Long id) {
    return jpaRepository.existsById(id);
  }

  @Nonnull
  public List<TenantBillingEntity> findAll() {
    return jpaRepository.findAll();
  }

  public void deleteById(@Nonnull final Long id) {
    jpaRepository.deleteById(id);
  }
}