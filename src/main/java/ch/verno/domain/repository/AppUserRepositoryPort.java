package ch.verno.domain.repository;

import ch.verno.domain.model.user.AppUser;
import jakarta.annotation.Nonnull;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepositoryPort {

  @Nonnull
  Optional<AppUser> findById(@Nonnull UUID id);

  @Nonnull
  Optional<AppUser> findByEmail(@Nonnull String email);

  boolean existsByEmail(@Nonnull String email);

  @Nonnull
  AppUser save(@Nonnull AppUser appUser);

  void deleteById(@Nonnull UUID id);
}