package ch.verno.server.service;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.server.mapper.AppUserMapper;
import ch.verno.server.repository.AppUserRepository;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService implements IAppUserService {

  @Nonnull
  private final AppUserRepository appUserRepository;

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
  public AppUserDto save(@Nonnull final AppUserDto user) {
    final var save = appUserRepository.save(AppUserMapper.toEntity(user));
    return AppUserMapper.toDto(save);
  }

  @Nonnull
  @Override
  public Optional<AppUserDto> findByUserName(@Nonnull final String username) {
    final var entityByUserName = findEntityByUserName(username);
    return entityByUserName.map(AppUserMapper::toDto);
  }

  private Optional<AppUserEntity> findEntityByUserName(@Nonnull final String username) {
    return appUserRepository.findByUsername(username);
  }
}

