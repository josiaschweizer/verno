package ch.verno.server.repository;

import ch.verno.db.entity.AppUserEntity;
import ch.verno.db.jpa.SpringDataAppUserJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AppUserRepository {

  @Nonnull
  private final SpringDataAppUserJpaRepository springDataAppUserJpaRepository;

  public AppUserRepository(@Nonnull final SpringDataAppUserJpaRepository springDataAppUserJpaRepository) {
    this.springDataAppUserJpaRepository = springDataAppUserJpaRepository;
  }

  @Nonnull
  public Optional<AppUserEntity> findByUsername(@Nonnull final String username) {
    return springDataAppUserJpaRepository.findByUsername(username);
  }

  @Nonnull
  public AppUserEntity save(@Nonnull final AppUserEntity user) {
    return springDataAppUserJpaRepository.save(user);
  }
}