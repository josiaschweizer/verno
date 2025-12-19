package ch.verno.server.persistence.adapter;

import ch.verno.domain.model.user.AppUser;
import ch.verno.domain.repository.AppUserRepositoryPort;
import ch.verno.server.persistence.mapper.AppUserMapper;
import ch.verno.server.persistence.repository.AppUserRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AppUserJpaAdapter implements AppUserRepositoryPort {

  @Nonnull
  private final AppUserRepository repo;

  public AppUserJpaAdapter(@Nonnull final AppUserRepository repo) {
    this.repo = repo;
  }

  @Nonnull
  @Override
  public Optional<AppUser> findById(@Nonnull final UUID id) {
    return repo.findById(id).map(AppUserMapper::toDomain);
  }

  @Nonnull
  @Override
  public Optional<AppUser> findByEmail(@Nonnull final String email) {
    return repo.findByEmail(email).map(AppUserMapper::toDomain);
  }

  @Override
  public boolean existsByEmail(@Nonnull final String email) {
    return repo.existsByEmail(email);
  }

  @Nonnull
  @Override
  public AppUser save(@Nonnull final AppUser appUser) {
    final var saved = repo.save(AppUserMapper.toEntity(appUser));
    return AppUserMapper.toDomain(saved);
  }

  @Override
  public void deleteById(@Nonnull final UUID id) {
    repo.deleteById(id);
  }
}