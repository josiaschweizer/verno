package ch.verno.ui.lib.helper;

import ch.verno.publ.Routes;
import com.vaadin.flow.component.UI;
import org.springframework.security.core.context.SecurityContextHolder;

public class LogoutHelper {

  public static void logout() {
    final var ui = UI.getCurrent();

    SecurityContextHolder.clearContext();
    ui.getSession().getSession().invalidate();
    ui.getPage().setLocation(Routes.LOGIN);
  }
}
