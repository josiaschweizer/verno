package ch.verno.ui.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class HomeView extends VerticalLayout {

  public HomeView() {
    add("Welcome to Verno!");
    setPadding(true);
    setSpacing(true);
  }

}
