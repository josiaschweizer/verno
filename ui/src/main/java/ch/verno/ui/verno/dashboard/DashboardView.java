package ch.verno.ui.verno.dashboard;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.components.toolbar.ViewToolbar;
import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@PermitAll
@PageTitle("Dashboard")
public class DashboardView extends VerticalLayout {

  @Autowired
  public DashboardView(@Nonnull final GlobalInterface globalInterface) {
    setSizeFull();
    setPadding(false);
    setSpacing(false);
    setAlignItems(Alignment.STRETCH);

    final var dashboard = new Dashboard(globalInterface);

    add(createViewToolBar(globalInterface));
    add(dashboard);
  }

  @Nonnull
  private ViewToolbar createViewToolBar(@Nonnull final GlobalInterface globalInterface) {
    return ViewToolbarFactory.createSimpleToolbar(globalInterface, getTranslation("shared.dashboard"));
  }

}
