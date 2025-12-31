package ch.verno.common.db.service;

import ch.verno.common.db.dto.AppUserDto;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IAppUserService extends UserDetailsService {

  @Nonnull
  AppUserDto findByUserName(@Nonnull String username);

}
