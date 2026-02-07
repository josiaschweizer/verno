package ch.verno.common.db.service;

import ch.verno.common.db.dto.table.AppUserDto;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface IAppUserService extends UserDetailsService {

  @Nonnull
  Optional<AppUserDto> findByUserName(@Nonnull String username);

  @Nonnull
  AppUserDto findAppUserById(@Nonnull Long id);

  @Nonnull
  List<AppUserDto> getAllAppUsers();

  @Nonnull
  AppUserDto createAppUser(@Nonnull AppUserDto user);

  @Nonnull
  AppUserDto updateAppUser(@Nonnull AppUserDto user);

}