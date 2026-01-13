package ch.verno.ui.verno.io;

import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(Routes.IO)
@Menu(order = 4, icon = "vaadin:inbox", title = "Import / Export")
public class ImportExport extends VerticalLayout {

  public ImportExport() {

    initUI();
  }

  private void initUI() {
    setSizeFull();
    setPadding(false);
    setMargin(false);

    add(ViewToolbarFactory.createSimpleToolbar("Import / Export"));

    final var span = new Span("Diese Seite befindet sich im Aufbau.");
    span.getStyle().setFontSize(LumoUtility.FontSize.XXLARGE);
    add(span);
  }

}
