package ch.verno.server.properties.user;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.dto.table.AppUserSettingDto;
import ch.verno.common.server.service.intern.user.IAppUserService;
import ch.verno.common.server.service.intern.user.IAppUserSettingService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.properties.UserProperties;
import ch.verno.lib.Lazy;
import ch.verno.publ.Routes;
import com.vaadin.flow.component.UI;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Optional;

@Service
public class UserPropertiesImpl implements UserProperties {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final Lazy<IAppUserService> appUserService;
  @Nonnull private final Lazy<IAppUserSettingService> appUserSettingService;

  public UserPropertiesImpl(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;
    this.appUserService = Lazy.of(() -> globalInterface.getService(IAppUserService.class));
    this.appUserSettingService = Lazy.of(() -> globalInterface.getService(IAppUserSettingService.class));
  }

  /**
   *
   * @return the current user, or empty if no user is authenticated
   */
  @Nonnull
  @Override
  public Optional<AppUserDto> getOptionalCurrentUser() {
    final var authentication = SecurityContextHolder.getContext()
            .getAuthentication();
    if (authentication == null) {
      return Optional.empty();
    }

    return appUserService.get().findByUserName(authentication
            .getName());
  }

  /**
   *
   * @return the current user, or throws an exception if no user is authenticated
   * @throws IllegalStateException if no user is authenticated
   */
  @Nonnull
  @Override
  public AppUserDto getCurrentUser() {
    final var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new IllegalStateException("No user is authenticated");
    }

    final var found = appUserService.get().findByUserName(authentication.getName());
    if (found.isEmpty()) {
      throw new IllegalStateException("Authenticated user not found in database: " + authentication.getName());
    }

    return found.get();
  }

  /**
   * uses {@code getCurrentUser} for getting the user and then makes a callback onto the user setting table
   *
   * @return the current user setting
   */
  @Nonnull
  @Override
  public AppUserSettingDto getCurrentUserSetting() {
    final var currentUser = getCurrentUser();
    return appUserSettingService.get().getAppUserSettingByUserId(currentUser.getId());
  }

  /**
   * Returns the currently authenticated Spring Security user.
   *
   * @return the authenticated Spring {@link User},
   *         or {@code null} if no user is authenticated or
   *         if the principal is not of type {@link User}
   */
  @Nullable
  @Override
  public User getCurrentSpringUser() {
    final var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof User) {
      return (User) authentication.getPrincipal();
    }

    return null;
  }

  /**
   * Returns the currently authenticated Spring Security user.
   *
   * @return the authenticated Spring {@link User}
   * @throws IllegalStateException if no user is authenticated
   *                               or if the principal is not of type {@link User}
   */
  @Nonnull
  @Override
  public User getCurrentSpringUserNonnull() {
    final var user = getCurrentSpringUser();
    if (user == null) {
      throw new IllegalStateException("No user is authenticated");
    }

    return user;
  }

  /**
   * logout current user and redirect to login page
   *
   */
  @Override
  public void logout() {
    final var ui = UI.getCurrent();

    SecurityContextHolder.clearContext();
    ui.getSession().getSession().invalidate();
    ui.getPage().setLocation(Routes.LOGIN);
  }

}
