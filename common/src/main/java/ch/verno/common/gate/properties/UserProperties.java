package ch.verno.common.gate.properties;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.Routes;
import com.vaadin.flow.component.UI;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProperties {

  @Nonnull private final GlobalInterface globalInterface;

  public UserProperties(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;
  }

  /**
   *
   * @return the current user, or empty if no user is authenticated
   */
  @Nonnull
  public Optional<AppUserDto> getOptionalCurrentUser() {
    final var authentication = SecurityContextHolder.getContext()
            .getAuthentication();
    if (authentication == null) {
      return Optional.empty();
    }

    final var appUserService = globalInterface.getService(IAppUserService.class);
    return appUserService.findByUserName(authentication
            .getName());
  }

  /**
   *
   * @return the current user, or throws an exception if no user is authenticated
   * @throws IllegalStateException if no user is authenticated
   */
  @Nonnull
  public AppUserDto getCurrentUser() {
    final var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new IllegalStateException("No user is authenticated");
    }

    final var appUserService = globalInterface.getService(IAppUserService.class);
    final var found = appUserService.findByUserName(authentication.getName());
    if (found.isEmpty()) {
      throw new IllegalStateException("Authenticated user not found in database: " + authentication.getName());
    }

    return found.get();
  }

  public void logout() {
    final var ui = UI.getCurrent();

    SecurityContextHolder.clearContext();
    ui.getSession().getSession().invalidate();
    ui.getPage().setLocation(Routes.LOGIN);
  }

}
