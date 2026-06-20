package ch.verno.db.jpa.tenant;

import ch.verno.db.entity.setting.TenantSettingEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;

public interface SpringDataTenantSettingJpaRepository extends AbstractEntityJpaRepository<TenantSettingEntity, Long> {
}
