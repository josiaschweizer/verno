package ch.verno.ui.i18n;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.Nonnull;

import java.util.Locale;

public final class VernoServiceInitListener implements VaadinServiceInitListener {

  @Override
  public void serviceInit(@Nonnull final ServiceInitEvent event) {
    event.getSource().addUIInitListener(uiEvent -> {
      final UI ui = uiEvent.getUI();
      final Locale preferred = VaadinSession.getCurrent().getLocale();
      ui.setLocale(preferred != null ? preferred : Locale.GERMAN);
    });
  }
}