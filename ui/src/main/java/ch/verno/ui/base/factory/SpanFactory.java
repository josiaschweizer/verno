package ch.verno.ui.base.factory;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.dom.Style;
import jakarta.annotation.Nonnull;

public class SpanFactory {

  /**
   * Creates a Span component that combines an icon and text, aligned horizontally with spacing.
   * perfect for use in buttons, labels or context menus
   *
   * @param text       the text to display alongside the icon
   * @param vaadinIcon the VaadinIcon to display
   * @return a Span component containing the icon and text
   */
  @Nonnull
  public static Span createSpan(@Nonnull final String text,
                                @Nonnull final VaadinIcon vaadinIcon) {
    final var icon = vaadinIcon.create();
    icon.getStyle()
            .setWidth("14px")
            .setHeight("14px");

    final var textSpan = new Span(text);

    final var wrapper = new Span(icon, textSpan);
    wrapper.getStyle()
            .setDisplay(Style.Display.FLEX)
            .setAlignItems(Style.AlignItems.CENTER)
            .setGap("var(--lumo-space-s)");

    return wrapper;
  }

}
