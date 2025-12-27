package ch.verno.ui.verno.dashboard;

import ch.verno.ui.base.components.toolbar.ViewToolbar;
import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

@Route("")
@PermitAll
@PageTitle("Dashboard")
public class DashboardPage extends VerticalLayout {

  public DashboardPage() {
    this.setWidthFull();
    this.setPadding(false);
    this.setSpacing(false);

    add(createViewToolBar());
    add(new H1("Welcome to the Dashboard"));
    setSizeFull();
    setAlignItems(Alignment.CENTER);
  }

  @Nonnull
  private ViewToolbar createViewToolBar() {
    return ViewToolbarFactory.createSimpleToolbar("Dashboard");
  }

}
