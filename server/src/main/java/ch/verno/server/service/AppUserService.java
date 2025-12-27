package ch.verno.server.service;

import ch.verno.common.db.service.IAppUserService;
import ch.verno.server.repository.AppUserRepository;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Profile("!dev")
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
}

