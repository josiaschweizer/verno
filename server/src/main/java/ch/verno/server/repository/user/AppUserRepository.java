package ch.verno.server.repository.user;

import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.db.jpa.user.SpringDataAppUserJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AppUserRepository extends AbstractEntityRepository<AppUserEntity, Long, SpringDataAppUserJpaRepository> {

  public AppUserRepository(@Nonnull final SpringDataAppUserJpaRepository repository) {
    super(repository);
  }

  @Nonnull
  public Optional<AppUserEntity> findByUsername(@Nonnull final String username) {
    return getRepository().findByUsername(username);
  }

  @Nonnull
  public Optional<AppUserEntity> findByUsernameOrEmail(@Nonnull final String usernameOrEmail) {
    final var userByUsername = getRepository().findByUsername(usernameOrEmail);

    if (userByUsername.isPresent()) {
      return userByUsername;
    }

    return getRepository().findByEmail(usernameOrEmail);
  }

  public boolean existsByUsernameAndTenantId(@Nonnull final String username,
                                             @Nonnull final Long tenantId) {
    return getRepository().existsByUsernameAndTenantId(username, tenantId); //TODO check if the tenant can be removed -> tenant is always filtered<
  }
}