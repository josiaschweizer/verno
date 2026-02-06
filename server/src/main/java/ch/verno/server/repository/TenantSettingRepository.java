package ch.verno.server.repository;

import ch.verno.db.entity.setting.TenantSettingEntity;
import ch.verno.db.jpa.SpringDataTenantSettingJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TenantSettingRepository {

  @Nonnull
  private final SpringDataTenantSettingJpaRepository springDataTenantSettingJpaRepository;

  public TenantSettingRepository(@Nonnull final SpringDataTenantSettingJpaRepository springDataTenantSettingJpaRepository) {
    this.springDataTenantSettingJpaRepository = springDataTenantSettingJpaRepository;
  }

  @Nonnull
  public Optional<TenantSettingEntity> findById(@Nonnull final Long id) {
    return springDataTenantSettingJpaRepository.findById(id);
  }

  @Nonnull
  public List<TenantSettingEntity> findAll() {
    return springDataTenantSettingJpaRepository.findAll();
  }

  @Nonnull
  public TenantSettingEntity save(@Nonnull final TenantSettingEntity entity) {
    return springDataTenantSettingJpaRepository.save(entity);
  }
}