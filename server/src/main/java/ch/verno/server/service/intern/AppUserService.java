package ch.verno.server.service.intern;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.filter.AppUserFilter;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.common.tenant.TenantContext;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.server.mapper.AppUserMapper;
import ch.verno.server.repository.AppUserRepository;
import ch.verno.server.spec.AppUserSpec;
import ch.verno.server.spec.PageHelper;
import com.vaadin.flow.data.provider.QuerySortOrder;
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

  @Nonnull private final AppUserRepository appUserRepository;
  @Nonnull private final AppUserSpec appUserSpec;

  @Nonnull
  @PersistenceContext
  private EntityManager entityManager;

  public AppUserService(@Nonnull final AppUserRepository appUserRepository) {
    this.appUserRepository = appUserRepository;

    this.appUserSpec = new AppUserSpec();
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(@Nonnull final String username) throws UsernameNotFoundException {
    final var user = appUserRepository.findByUsernameOrEmail(username)
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
    return appUserRepository.findByUsername(username);
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
  @Transactional(readOnly = true)
  public List<AppUserDto> findUsers(@Nonnull final AppUserFilter filter,
                                    final int offset,
                                    final int limit,
                                    @Nonnull final List<QuerySortOrder> sortOrders) {
    final var pageable = PageHelper.createPageRequest(offset, limit, sortOrders);
    final var spec = appUserSpec.appUserSpec(filter);

    return appUserRepository.findAll(spec, pageable).stream()
            .map(AppUserMapper::toDto)
            .toList();
  }

  @Override
  @Transactional
  public void createAppUser(@Nonnull final AppUserDto user) {
    final long tenantId = TenantContext.getRequired();

    final TenantEntity tenantRef = entityManager.getReference(TenantEntity.class, tenantId);
    final AppUserEntity entity = AppUserMapper.toEntity(user, tenantRef);

    final var saved = appUserRepository.save(entity);
    AppUserMapper.toDto(saved);
  }

  @Override
  @Transactional
  public void updateAppUser(@Nonnull final AppUserDto user) {
    if (user.getId() == null) {
      throw new IllegalArgumentException("user.id must not be null for update");
    }

    final var entity = appUserRepository.findById(user.getId()).orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.APP_USER_NOT_FOUND, user.getId()));
    entity.setUsername(user.getUsername());
    entity.setFirstname(user.getFirstname());
    entity.setLastname(user.getLastname());
    entity.setEmail(user.getEmail());
    entity.setRole(user.getRole().getRole());

    if (!user.getPasswordHash().isBlank()) {
      entity.setPasswordHash(user.getPasswordHash());
    }

    final var saved = appUserRepository.save(entity);
    AppUserMapper.toDto(saved);
  }

  @Override
  @Transactional
  public void changePassword(@Nonnull final Long userId, @Nonnull final String newPassword) {
    final var found = appUserRepository.findById(userId).orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.APP_USER_NOT_FOUND, userId));
    found.setPasswordHash(newPassword);

    appUserRepository.save(found);
  }

  @Override
  @Transactional
  public void deleteAppUser(@Nonnull final Long id) {
    appUserRepository.deleteById(id);
  }
}