package ch.verno.db.jpa;

import ch.verno.db.entity.setting.AppUserSettingEntity;
import ch.verno.db.entity.user.AppUserEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SpringDataAppUserSettingJpaRepository extends
        JpaRepository<AppUserSettingEntity, Long>,
        JpaSpecificationExecutor<AppUserSettingEntity> {

  @Nonnull
  Optional<AppUserSettingEntity> findByUser(AppUserEntity userId);

}
