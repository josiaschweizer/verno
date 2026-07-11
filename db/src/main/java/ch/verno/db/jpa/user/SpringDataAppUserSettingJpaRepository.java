package ch.verno.db.jpa.user;

import ch.verno.db.entity.setting.AppUserSettingEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface SpringDataAppUserSettingJpaRepository extends AbstractEntityJpaRepository<AppUserSettingEntity, Long> {

  @Nonnull
  Optional<AppUserSettingEntity> findByUserId(Long userId);

}
