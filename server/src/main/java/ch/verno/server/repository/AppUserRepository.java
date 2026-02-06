package ch.verno.server.repository;

import ch.verno.db.entity.user.AppUserEntity;
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
  public Optional<AppUserEntity> findByUserName(@Nonnull final String username) {
    return springDataAppUserJpaRepository.findByUsername(username);
  }

  @Nonnull
  public Optional<AppUserEntity> findByUsernameMandantId(@Nonnull final String username,
                                                         @Nonnull final Long mandantId) {
    return springDataAppUserJpaRepository.findByUsernameAndMandant_Id(username, mandantId);
  }

  @Nonnull
  public Optional<AppUserEntity> findById(@Nonnull final Long id) {
    return springDataAppUserJpaRepository.findById(id);
  }

  @Nonnull
  public AppUserEntity save(@Nonnull final AppUserEntity user) {
    return springDataAppUserJpaRepository.save(user);
  }
}