package ch.verno.db.jpa;

import ch.verno.db.entity.user.AppUserEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SpringDataAppUserJpaRepository extends
        JpaSpecificationExecutor<AppUserEntity>,
        JpaRepository<AppUserEntity, Long> {

  @Nonnull
  Optional<AppUserEntity> findByUsername(@Nonnull final String username);

  Optional<AppUserEntity> findByUsernameAndTenant_Id(@Nonnull final String username, @Nonnull final Long tenantId);

  Optional<AppUserEntity> findByEmailAndTenant_Id(@Nonnull final String email, @Nonnull final Long tenantId);
}
