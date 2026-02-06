package ch.verno.ui.verno.security;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.service.IAppUserService;
import jakarta.annotation.Nonnull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  @Nonnull private final IAppUserService appUserService;
  @Nonnull private final PasswordEncoder passwordEncoder;

  public CustomAuthenticationProvider(
          @Nonnull final IAppUserService appUserService,
          @Nonnull final PasswordEncoder passwordEncoder) {
    this.appUserService = appUserService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(@Nonnull final Authentication authentication) throws AuthenticationException {

    if (!(authentication instanceof CustomUserPasswordAuthenticationToken token)) {
      throw new IllegalArgumentException("Unsupported authentication type");
    }

    final var userToken = (AppUserDto) token.getPrincipal();
    if (userToken == null) {
      throw new UsernameNotFoundException("User token is null");
    }

    final var username = userToken.getUsername();
    final var tenantId = userToken.getTenant();
    final var password = (String) token.getCredentials();

    final var appUser = loadUser(username, tenantId);

    if (!passwordEncoder.matches(password, appUser.getPasswordHash())) {
      throw new BadCredentialsException("Invalid password");
    }

    return new CustomUserPasswordAuthenticationToken(appUser, password);
  }

  @Override
  public boolean supports(final Class<?> authentication) {
    return CustomUserPasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }

  private AppUserDto loadUser(@Nonnull final String username, final Long tenantId) {
    if (tenantId != null) {
      return appUserService.findByUserNameMandantId(username, tenantId)
              .orElseThrow(() -> new UsernameNotFoundException(
                      "User '" + username + "' with tenant " + tenantId + " not found"));
    } else {
      return appUserService.findByUserName(username)
              .orElseThrow(() -> new UsernameNotFoundException(
                      "User '" + username + "' not found"));
    }
  }
}