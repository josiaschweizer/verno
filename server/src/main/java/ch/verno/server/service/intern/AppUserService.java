package ch.verno.server.service.intern;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.common.tenant.TenantContext;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.server.mapper.AppUserMapper;
import ch.verno.server.repository.AppUserRepository;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService implements IAppUserService {

  @Nonnull
  private final AppUserRepository appUserRepository;

  @PersistenceContext
  private EntityManager entityManager;

  public AppUserService(@Nonnull final AppUserRepository appUserRepository) {
    this.appUserRepository = appUserRepository;
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(@Nonnull final String username) throws UsernameNotFoundException {
    final var user = appUserRepository.findByUsername(username, TenantContext.getRequired())
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    return User.withUsername(user.getUsername())
            .password(user.getPasswordHash())
            .roles(user.getRole())
            .build();
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<AppUserDto> getAllAppUsers() {
    return appUserRepository.findAll().stream()
            .map(AppUserMapper::toDto)
            .toList();
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public Optional<AppUserDto> findByUserName(@Nonnull final String username) {
    return findEntityByUserName(username).map(AppUserMapper::toDto);
  }

  private Optional<AppUserEntity> findEntityByUserName(@Nonnull final String username) {
    return appUserRepository.findByUsername(username, TenantContext.getRequired());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public AppUserDto findAppUserById(@NonNull final Long id) {
    final var found = appUserRepository.findById(id);
    if (found.isEmpty()) {
      throw new DBNotFoundException(DBNotFoundReason.APP_USER_NOT_FOUND, id);
    }

    return AppUserMapper.toDto(found.get());
  }

  @Nonnull
  @Override
  @Transactional
  public AppUserDto createAppUser(@Nonnull final AppUserDto user) {
    final long tenantId = TenantContext.getRequired();

    final TenantEntity tenantRef = entityManager.getReference(TenantEntity.class, tenantId);
    final AppUserEntity entity = AppUserMapper.toEntity(user, tenantRef);

    final var saved = appUserRepository.save(entity);
    return AppUserMapper.toDto(saved);
  }

  @NonNull
  @Override
  public AppUserDto updateAppUser(@NonNull final AppUserDto user) {
    throw new IllegalArgumentException("Not implemented");
  }
}