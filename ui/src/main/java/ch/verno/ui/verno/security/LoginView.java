package ch.verno.ui.verno.security;

import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(Routes.LOGIN)
@AnonymousAllowed
public class LoginView extends VerticalLayout {

  public LoginView() {
    LoginOverlay login = new LoginOverlay();
    login.setAction("login");
    login.setOpened(true);
    login.setTitle("Verno");
    login.setForgotPasswordButtonVisible(false);
    login.setDescription("Please sign in");

    add(login);
  }
}