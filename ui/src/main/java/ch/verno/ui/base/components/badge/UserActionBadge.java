package ch.verno.ui.base.components.badge;

import ch.verno.publ.Publ;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;

import java.util.Locale;

public final class UserActionBadge extends Composite<HorizontalLayout> {

  private final Span badge;
  private final ContextMenu menu;

  public UserActionBadge(@Nonnull final String displayName) {
    final var layout = getContent();
    layout.setPadding(false);
    layout.setSpacing(false);

    badge = new Span(getInitial(displayName));
    badge.addClassNames(
            LumoUtility.FontWeight.SEMIBOLD,
            LumoUtility.FontSize.SMALL,
            LumoUtility.TextAlignment.CENTER
    );

    badge.getStyle()
            .setDisplay(Style.Display.INLINE_FLEX)
            .setAlignItems(Style.AlignItems.CENTER)
            .setJustifyContent(Style.JustifyContent.CENTER)
            .setWidth("35px")
            .setHeight("35px")
            .setBorderRadius("9999px")
            .setCursor("pointer")
            .setBorder("1px solid var(--lumo-contrast-20pct)")
            .setBackground("var(--lumo-contrast-5pct)")
            .set("user-select", "none");

    layout.add(badge);

    menu = new ContextMenu(badge);
    menu.setOpenOnClick(true);
  }

  @Nonnull
  public ContextMenu menu() {
    return menu;
  }

  @Nonnull
  public UserActionBadge addItem(@Nonnull final String text, @Nonnull final Runnable onClick) {
    menu.addItem(text, e -> onClick.run());
    return this;
  }

  @Nonnull
  public UserActionBadge addItem(@Nonnull final VaadinIcon icon,
                                 @Nonnull final String text,
                                 @Nonnull final Runnable onClick) {

    final var iconComponent = icon.create();
    final var textSpan = new Span(text);

    final var wrapper = new Span(iconComponent, textSpan);
    wrapper.getStyle()
            .setDisplay(Style.Display.FLEX)
            .setAlignItems(Style.AlignItems.CENTER)
            .setGap("var(--lumo-space-s)");

    menu.addItem(wrapper, e -> onClick.run());
    return this;
  }

  @Nonnull
  public UserActionBadge addContent(@Nonnull final Component component) {
    menu.addItem(component);
    return this;
  }

  @Nonnull
  private static String getInitial(@Nonnull final String displayName) {
    final var trimmed = displayName.trim();
    if (trimmed.isEmpty()) {
      return Publ.QUESTION_MARK;
    }

    final var parts = trimmed.split("\\s+");

    final char first = parts[0].charAt(0);

    if (parts.length >= 2 && !parts[1].isEmpty()) {
      final char second = parts[1].charAt(0);
      return (Publ.EMPTY_STRING + first + second).toUpperCase(Locale.ROOT);
    }

    return (Publ.EMPTY_STRING + first).toUpperCase(Locale.ROOT);
  }
}