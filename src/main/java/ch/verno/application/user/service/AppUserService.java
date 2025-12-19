package ch.verno.application.user.service;

import ch.verno.application.user.command.CreateAppUserCommand;
import ch.verno.domain.model.user.AppUser;
import ch.verno.domain.repository.AppUserRepositoryPort;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AppUserService {

  @Nonnull
  private final AppUserRepositoryPort repository;

  public AppUserService(@Nonnull final AppUserRepositoryPort repository) {
    this.repository = repository;
  }

  @Nonnull
  @Transactional
  public AppUser create(@Nonnull final CreateAppUserCommand cmd) {
    final var email = cmd.email();

    if (repository.existsByEmail(email)) {
      throw new IllegalArgumentException("Email already exists: " + email);
    }

    final var user = new AppUser(
        UUID.randomUUID(),
        email,
        Instant.now()
    );

    return repository.save(user);
  }

  @Nonnull
  @Transactional
  public AppUser createIfMissing(@Nonnull final String email) {
    final @Nullable AppUser existing = repository.findByEmail(email).orElse(null);
    if (existing != null) {
      return existing;
    }

    final var user = new AppUser(
        UUID.randomUUID(),
        email,
        Instant.now()
    );

    return repository.save(user);
  }

  @Transactional
  public void deleteById(@Nonnull final UUID id) {
    repository.deleteById(id);
  }
}