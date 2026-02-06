package ch.verno.ui.verno.security.login;

import ch.verno.common.lib.Routes;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.Nonnull;

@Route(Routes.LOGIN)
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

  @Nonnull private final LoginForm login;

  public LoginView() {
    this.login = new LoginForm();

    addClassName("login-view");
    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);

    login.setAction("login");
    login.setForgotPasswordButtonVisible(false);

    add(new H1("Verno Login"), login);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (event.getLocation()
            .getQueryParameters()
            .getParameters()
            .containsKey("error")) {
      login.setError(true);
    }
  }
}