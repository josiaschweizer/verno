package ch.verno.ui.verno.security;

import ch.verno.common.db.dto.AppUserDto;
import ch.verno.common.db.service.IAppUserService;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DevAppUserService implements IAppUserService {

  private final UserDetailsService delegate;

  public DevAppUserService(@Nonnull final UserDetailsService delegate) {
    this.delegate = delegate;
  }

  @Nonnull
  @Override
  public UserDetails loadUserByUsername(@Nonnull final String username) throws UsernameNotFoundException {
    return delegate.loadUserByUsername(username);
  }

  @Nonnull
  @Override
  public AppUserDto findByUserName(@Nonnull final String username) {
    final UserDetails user = loadUserByUsername(username);
    final String role = user.getAuthorities().stream()
            .findFirst()
            .map(a -> {
              final String r = a.getAuthority();
              return r.startsWith("ROLE_") ? r.substring(5) : r;
            })
            .orElse("USER");

    return new AppUserDto(user.getUsername(), role);
  }
}

