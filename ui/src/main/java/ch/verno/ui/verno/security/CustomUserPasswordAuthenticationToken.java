package ch.verno.ui.verno.security;

import ch.verno.common.db.dto.table.AppUserDto;
import jakarta.annotation.Nonnull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class CustomUserPasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

  public CustomUserPasswordAuthenticationToken(@Nonnull final AppUserDto user,
                                               @Nonnull final String password) {
    super(user, password, List.of(new SimpleGrantedAuthority(user.getRole())));
  }
}
