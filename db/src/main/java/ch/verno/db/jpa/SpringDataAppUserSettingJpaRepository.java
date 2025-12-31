package ch.verno.db.jpa;

import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.db.entity.user.AppUserSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SpringDataAppUserSettingJpaRepository extends
        JpaRepository<AppUserSettingEntity, Long>,
        JpaSpecificationExecutor<AppUserSettingEntity> {

  Optional<AppUserSettingEntity> findByUser(AppUserEntity userId);

}
