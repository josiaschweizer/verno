package ch.verno.server.persistence;

import ch.verno.domain.model.user.AppUser;
import ch.verno.domain.repository.AppUserStore;
import ch.verno.server.persistence.mapper.AppUserMapper;
import ch.verno.server.persistence.repository.AppUserRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AppUserStoreJpaAdapter implements AppUserStore {

  private final @Nonnull AppUserRepository repo;

  public AppUserStoreJpaAdapter(@Nonnull final AppUserRepository repo) {
    this.repo = repo;
  }

  @Override
  public @Nonnull Optional<AppUser> findById(@Nonnull final UUID id) {
    return repo.findById(id).map(AppUserMapper::toDomain);
  }

  @Override
  public @Nonnull Optional<AppUser> findByEmail(@Nonnull final String email) {
    return repo.findByEmail(email).map(AppUserMapper::toDomain);
  }

  @Override
  public boolean existsByEmail(@Nonnull final String email) {
    return repo.existsByEmail(email);
  }

  @Override
  public @Nonnull AppUser save(@Nonnull final AppUser user) {
    final var saved = repo.save(AppUserMapper.toEntity(user));
    return AppUserMapper.toDomain(saved);
  }
}