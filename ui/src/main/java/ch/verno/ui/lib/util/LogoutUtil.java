package ch.verno.ui.lib.util;

import ch.verno.common.lib.Routes;
import ch.verno.lib.Lazy;
import ch.verno.rpc.properties.user.UserProperties;
import ch.verno.ui.base.navigation.Navigator;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.UI;
import jakarta.annotation.Nonnull;

public class LogoutUtil {

  @Nonnull private final Lazy<UserProperties> userProperties;

  @Inject
  public LogoutUtil(@Nonnull final Injector injector) {
    this.userProperties = Lazy.of(() -> injector.getInstance(UserProperties.class));
  }

  public void logout() {
    final var ui = UI.getCurrent();
    final var result = userProperties.get().logout();
    if (result) {
      ui.getSession().getSession().invalidate();
      Navigator.navigateTo(Routes.LOGIN);
    }
  }

}
