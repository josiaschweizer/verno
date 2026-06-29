package ch.verno.ui.injection.scope;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class PageScopeInitListener implements VaadinServiceInitListener {

  @Override
  public void serviceInit(final ServiceInitEvent event) {
    event.getSource().addUIInitListener(uiInitEvent -> {
      final var ui = uiInitEvent.getUI();
      ui.addBeforeEnterListener(navEvent -> PageScope.resetCurrentPage());
    });
  }
}