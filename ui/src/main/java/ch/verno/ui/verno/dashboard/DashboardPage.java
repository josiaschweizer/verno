package ch.verno.ui.verno.dashboard;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("Dashboard")
public class DashboardPage extends VerticalLayout {

  public DashboardPage() {
    add(new H1("Welcome to the Dashboard"));
    setSizeFull();
    setAlignItems(Alignment.CENTER);
  }

}
