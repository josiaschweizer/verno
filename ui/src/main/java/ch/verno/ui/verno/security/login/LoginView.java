package ch.verno.ui.verno.security.login;

import ch.verno.common.lib.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.Nonnull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

@Route(Routes.LOGIN)
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

  @Nonnull private final LoginForm login;
  @Nonnull private final AuthenticationManager authenticationManager;

  public LoginView(@Nonnull final AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
    this.login = new LoginForm();

    addClassName("login-view");
    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);

    login.setForgotPasswordButtonVisible(false);
    login.addLoginListener(e -> {
      try {
        final var authToken = new UsernamePasswordAuthenticationToken(
                e.getUsername(),
                e.getPassword()
        );

        final Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UI.getCurrent().navigate("");
      } catch (AuthenticationException ex) {
        login.setError(true);
      }
    });

    add(new H1("Verno Login"), login);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (SecurityContextHolder.getContext().getAuthentication() != null
            && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
            && !Objects.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal(), "anonymousUser")) {
      event.forwardTo("");
    }

    if (event.getLocation()
            .getQueryParameters()
            .getParameters()
            .containsKey("error")) {
      login.setError(true);
    }
  }
}