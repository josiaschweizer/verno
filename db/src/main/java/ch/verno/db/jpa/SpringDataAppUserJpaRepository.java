package ch.verno.db.jpa;

import ch.verno.db.entity.user.AppUserEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataAppUserJpaRepository extends JpaRepository<AppUserEntity, Long> {

  @Nonnull
  Optional<AppUserEntity> findByUsername(@Nonnull final String username);

  Optional<AppUserEntity> findByUsernameAndMandant_Id(@Nonnull final String username, @Nonnull final Long mandantId);
}
