package ch.verno.server.repository;

import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.db.jpa.SpringDataAppUserJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AppUserRepository {

  @Nonnull private final SpringDataAppUserJpaRepository jpaRepository;

  public AppUserRepository(@Nonnull final SpringDataAppUserJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  public List<AppUserEntity> findAll() {
    return jpaRepository.findAll();
  }

  public Page<AppUserEntity> findAll(@Nonnull final Specification<AppUserEntity> spec,
                                     @Nonnull final Pageable pageable) {
    return jpaRepository.findAll(spec, pageable);
  }

  @Nonnull
  public Optional<AppUserEntity> findByUsername(@Nonnull final String username,
                                                @Nonnull final Long tenantId) {
    return jpaRepository.findByUsernameAndTenant_Id(username, tenantId);
  }

  @Nonnull
  public Optional<AppUserEntity> findByUsernameOrEmail(@Nonnull final String usernameOrEmail,
                                                       @Nonnull final Long tenantId) {
    final var userByUsername = jpaRepository.findByUsernameAndTenant_Id(usernameOrEmail, tenantId);
    if (userByUsername.isPresent()) {
      return userByUsername;
    }

    return jpaRepository.findByEmailAndTenant_Id(usernameOrEmail, tenantId);
  }

  @Nonnull
  public Optional<AppUserEntity> findByUsernameWithoutMandantContext(@Nonnull final String username) {
    return jpaRepository.findByUsername(username);
  }

  @Nonnull
  public Optional<AppUserEntity> findById(@Nonnull final Long id) {
    return jpaRepository.findById(id);
  }

  public void deleteById(@Nonnull final Long id) {
    jpaRepository.deleteById(id);
  }

  @Nonnull
  public AppUserEntity save(@Nonnull final AppUserEntity user) {
    return jpaRepository.save(user);
  }


  public void flush() {
    jpaRepository.flush();
  }
}