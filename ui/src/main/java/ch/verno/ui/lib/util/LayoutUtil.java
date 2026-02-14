package ch.verno.ui.lib.util;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

public class LayoutUtil {

  @Nonnull
  public static VerticalLayout createVerticalLayoutFromComponents(@Nonnull final Component... components) {
    final var layout = new VerticalLayout(components);
    layout.setPadding(false);
    layout.setSpacing(false);
    layout.setWidthFull();
    return layout;
  }

  @Nonnull
  public static HorizontalLayout createHorizontalLayoutFromComponents(@Nonnull final Component... components) {
    final var layout = new HorizontalLayout();
    layout.setWidthFull();

    layout.getStyle().set("flex-wrap", "wrap");
    layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.START);

    for (final var component : components) {
      component.getElement().getStyle().set("min-width", "260px");
      component.getElement().getStyle().set("flex", "1 1 260px");
      layout.add(component);
    }

    return layout;
  }

}
