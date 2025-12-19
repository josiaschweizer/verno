package ch.verno.server.persistence.repository;

import ch.verno.server.persistence.entity.AppUserEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUserEntity, UUID> {

  @Nonnull
  Optional<AppUserEntity> findByEmail(@Nonnull String email);

  boolean existsByEmail(@Nonnull String email);

  void deleteByEmail(@Nonnull String email);

  @Nullable
  AppUserEntity getByEmail(@Nonnull String email);
}