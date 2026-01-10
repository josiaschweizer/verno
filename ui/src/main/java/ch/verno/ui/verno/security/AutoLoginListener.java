package ch.verno.ui.verno.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class AutoLoginListener implements VaadinServiceInitListener {

  private static final Logger LOG = LoggerFactory.getLogger(AutoLoginListener.class);

  @Nonnull
  private final AutoLoginProperties autoLoginProperties;

  @Nonnull
  private final AuthenticationManager authenticationManager;

  @Nonnull
  private final AuthenticationContext authenticationContext;

  public AutoLoginListener(@Nonnull final AutoLoginProperties autoLoginProperties,
                           @Nonnull final AuthenticationManager authenticationManager,
                           @Nonnull final AuthenticationContext authenticationContext) {
    this.autoLoginProperties = autoLoginProperties;
    this.authenticationManager = authenticationManager;
    this.authenticationContext = authenticationContext;
  }

  @Override
  public void serviceInit(@Nonnull final ServiceInitEvent event) {
    event.getSource().addUIInitListener(uiInitEvent -> {
      if (!autoLoginProperties.isAutoLoginEnabled()) {
        return;
      }

      if (authenticationContext.isAuthenticated()) {
        return;
      }

      performAutoLogin(uiInitEvent.getUI());
    });
  }

  private void performAutoLogin(@Nonnull final UI ui) {
    final var username = autoLoginProperties.getUsername();
    final var password = autoLoginProperties.getPassword();

    if (username == null || password == null) {
      return;
    }

    try {
      final var authToken = new UsernamePasswordAuthenticationToken(username, password);
      final var authentication = authenticationManager.authenticate(authToken);

      final var securityContext = SecurityContextHolder.createEmptyContext();
      securityContext.setAuthentication(authentication);
      SecurityContextHolder.setContext(securityContext);

      ui.getSession().getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

      LOG.info("Auto-Login erfolgreich für Benutzer: {}", username);

      ui.getPage().setLocation("/");

    } catch (final Exception e) {
      LOG.error("Auto-Login fehlgeschlagen für Benutzer: {}", username, e);
    }
  }
}

