package ch.verno.db.jpa.tenant;

import ch.verno.db.entity.setting.TenantSettingEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface SpringDataTenantSettingJpaRepository extends AbstractEntityJpaRepository<TenantSettingEntity, Long> {

  @Nonnull
  Optional<TenantSettingEntity> findByTenant_Id(@Nonnull Long tenantId);

}
