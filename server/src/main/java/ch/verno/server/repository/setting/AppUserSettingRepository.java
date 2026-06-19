package ch.verno.server.repository.setting;

import ch.verno.db.entity.setting.AppUserSettingEntity;
import ch.verno.db.jpa.user.SpringDataAppUserSettingJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class AppUserSettingRepository extends AbstractEntityRepository<
        AppUserSettingEntity,
        Long,
        SpringDataAppUserSettingJpaRepository> {

  public AppUserSettingRepository(@Nonnull final SpringDataAppUserSettingJpaRepository repository) {
    super(repository);
  }

  @Nonnull
  public Optional<AppUserSettingEntity> findByUserId(@Nonnull final Long userId) {
    return getRepository().findByUserId(userId);
  }

}
