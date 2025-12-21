package ch.verno.application.user.service;

import ch.verno.application.user.command.CreateAppUserCommand;
import ch.verno.domain.model.user.AppUser;
import ch.verno.domain.repository.AppUserRepositoryPort;
import jakarta.annotation.Nonnull;
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
  private static String requireValidEmail(@Nonnull final String email) {
    final var trimmed = email.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("Email must not be blank");
    }

    final var at = trimmed.indexOf('@');
    final var dot = trimmed.lastIndexOf('.');
    if (at <= 0 || dot <= at + 1 || dot == trimmed.length() - 1) {
      throw new IllegalArgumentException("Email is not valid: " + trimmed);
    }

    return trimmed;
  }

  @Nonnull
  @Transactional
  public AppUser createIfMissing(@Nonnull final String email) {
    final AppUser existingUser = repository.findByEmail(email).orElse(null);
    if (existingUser != null) {
      return existingUser;
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