package ch.verno.ui.lib.util;

import ch.verno.ui.base.components.layout.horizontal.VAHorizontalLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import jakarta.annotation.Nonnull;

public class LayoutUtil {

  @Nonnull
  public static VerticalLayout createVertical(@Nonnull final Component... components) {
    final var layout = new VerticalLayout(components);
    layout.setPadding(false);
    layout.setSpacing(false);
    layout.setWidthFull();
    return layout;
  }

  @Nonnull
  public static VAHorizontalLayout createHorizontal(@Nonnull final Component... components) {
    final var layout = new VAHorizontalLayout();
    layout.setWidthFull();

    layout.getStyle().setFlexWrap(Style.FlexWrap.WRAP);
    layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.START);

    for (final var component : components) {
      component.getElement().getStyle().set("min-width", "260px");
      component.getElement().getStyle().set("flex", "1 1 260px");
      layout.add(component);
    }

    return layout;
  }

}
