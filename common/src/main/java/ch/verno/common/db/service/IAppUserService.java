package ch.verno.common.db.service;

import ch.verno.common.db.dto.table.AppUserDto;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface IAppUserService extends UserDetailsService {

  @Nonnull
  AppUserDto save(@Nonnull AppUserDto user);

  @Nonnull
  Optional<AppUserDto> findByUserName(@Nonnull String username);

}
