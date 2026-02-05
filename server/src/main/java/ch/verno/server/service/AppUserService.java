package ch.verno.server.service;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.mandant.MandantContext;
import ch.verno.db.entity.mandant.MandantEntity;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.server.mapper.AppUserMapper;
import ch.verno.server.repository.AppUserRepository;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AppUserService implements IAppUserService {

  @Nonnull
  private final AppUserRepository appUserRepository;

  @PersistenceContext
  private EntityManager em;

  public AppUserService(@Nonnull final AppUserRepository appUserRepository) {
    this.appUserRepository = appUserRepository;
  }

  @Nonnull
  @Override
  public UserDetails loadUserByUsername(@Nonnull final String username) throws UsernameNotFoundException {
    final var user = appUserRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    return User.withUsername(user.getUsername())
            .password(user.getPasswordHash())
            .roles(user.getRole())
            .build();
  }

  @Nonnull
  @Override
  @Transactional
  public AppUserDto save(@Nonnull final AppUserDto user) {
    final long mandantId = MandantContext.getRequired();

    final MandantEntity mandantRef = em.getReference(MandantEntity.class, mandantId);
    final AppUserEntity entity = AppUserMapper.toEntity(user, mandantRef);

    final var saved = appUserRepository.save(entity);
    return AppUserMapper.toDto(saved);
  }

  @Nonnull
  @Override
  public Optional<AppUserDto> findByUserName(@Nonnull final String username) {
    return findEntityByUserName(username).map(AppUserMapper::toDto);
  }

  private Optional<AppUserEntity> findEntityByUserName(@Nonnull final String username) {
    return appUserRepository.findByUsername(username);
  }

  @Deprecated
  @Transactional
  public void saveForSeed(@Nonnull final AppUserDto user) {
    final long mandantId = 7777L;

    final MandantEntity mandantRef = em.getReference(MandantEntity.class, mandantId);
    final AppUserEntity entity = AppUserMapper.toEntity(user, mandantRef);

    appUserRepository.save(entity);
  }
}