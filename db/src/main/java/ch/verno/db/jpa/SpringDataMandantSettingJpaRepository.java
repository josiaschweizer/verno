package ch.verno.db.jpa;

import ch.verno.db.entity.setting.MandantSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMandantSettingJpaRepository extends JpaRepository<MandantSettingEntity, Long> {
}
