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

  Optional<AppUserEntity> findByEmail(@Nonnull final String email);

}
