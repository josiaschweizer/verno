package ch.verno.server.repository.setting;

import ch.verno.db.entity.setting.TenantSettingEntity;
import ch.verno.db.jpa.tenant.SpringDataTenantSettingJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;

public class TenantSettingRepository extends AbstractEntityRepository<
        TenantSettingEntity,
        Long,
        SpringDataTenantSettingJpaRepository> {

  public TenantSettingRepository(@Nonnull final SpringDataTenantSettingJpaRepository repository) {
    super(repository);
  }

}
