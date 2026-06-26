package ch.verno.server.repository.setting;

import ch.verno.db.entity.setting.TenantSettingEntity;
import ch.verno.db.jpa.tenant.SpringDataTenantSettingJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class TenantSettingRepository extends AbstractEntityRepository<
        TenantSettingEntity,
        Long,
        SpringDataTenantSettingJpaRepository> {

  public TenantSettingRepository(@Nonnull final SpringDataTenantSettingJpaRepository repository) {
    super(repository);
  }

  @Nonnull
  public Optional<TenantSettingEntity> getByTenantId(@Nonnull final Long tenantId) {
    return getRepository().findByTenant_Id(tenantId);
  }

}
