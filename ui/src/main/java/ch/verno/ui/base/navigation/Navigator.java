package ch.verno.ui.base.navigation;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class Navigator {

  public static void navigateToVertical(@Nonnull final Class<? extends VerticalLayout> view) {
    UI.getCurrent().navigate(view);
  }

  public static void navigateToHorizontal(@Nonnull final Class<? extends HorizontalLayout> view){
    UI.getCurrent().navigate(view);
  }

  public static void navigateTo(@Nonnull final String href){
    UI.getCurrent().navigate(href);
  }

}
