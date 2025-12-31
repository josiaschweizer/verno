package ch.verno.server.repository;

import ch.verno.db.entity.user.AppUserSettingEntity;
import ch.verno.db.jpa.SpringDataAppUserJpaRepository;
import ch.verno.db.jpa.SpringDataAppUserSettingJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AppUserSettingRepository {

  @Nonnull
  private final SpringDataAppUserSettingJpaRepository jpaRepository;
  @Nonnull
  private final SpringDataAppUserJpaRepository appUserJpaRepository;

  public AppUserSettingRepository(@Nonnull final SpringDataAppUserSettingJpaRepository jpaRepository,
                                  @Nonnull final SpringDataAppUserJpaRepository appUserJpaRepository) {
    this.jpaRepository = jpaRepository;
    this.appUserJpaRepository = appUserJpaRepository;
  }

  @Nonnull
  public List<AppUserSettingEntity> findAll() {
    return jpaRepository.findAll();
  }

  @Nonnull
  public Optional<AppUserSettingEntity> findByUserId(@Nonnull final Long userId) {
    final var user = appUserJpaRepository.findById(userId);
    if (user.isEmpty()) {
      return Optional.empty();
    }
    return jpaRepository.findByUser(user.get());
  }

  @Nonnull
  public AppUserSettingEntity save(@Nonnull final AppUserSettingEntity entity) {
    return jpaRepository.save(entity);
  }

}
