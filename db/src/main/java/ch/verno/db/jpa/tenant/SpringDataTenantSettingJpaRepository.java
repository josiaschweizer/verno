package ch.verno.db.jpa.tenant;

import ch.verno.db.entity.setting.TenantSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTenantSettingJpaRepository extends
        JpaRepository<TenantSettingEntity, Long> {
}
