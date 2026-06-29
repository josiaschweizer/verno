package ch.verno.ui.base.components.badge;

import ch.verno.lib.Lazy;
import ch.verno.lib.New;
import ch.verno.lib.Publ;
import ch.verno.lib.VernoUtility;
import ch.verno.ui.base.components.span.VASpan;
import ch.verno.ui.base.shortcut.ShortcutDisplayUtil;
import ch.verno.ui.base.shortcut.VAShortcut;
import ch.verno.ui.base.shortcut.registry.ShortcutController;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.model.Cursor;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

public final class UserActionBadge extends Composite<HorizontalLayout> {

  @Nonnull private final Lazy<ShortcutController> shortcutRegistry;

  @Nonnull private final ContextMenu menu;
  @Nonnull private final List<EnabledBinding> enabledBindings;

  public UserActionBadge(@Nonnull final Lazy<ShortcutController> shortcutRegistry,
                         @Nonnull final String displayName) {
    this.shortcutRegistry = shortcutRegistry;
    this.enabledBindings = New.list();

    final var layout = getContent();
    layout.setPadding(false);
    layout.setSpacing(false);

    final var badge = new Span(getInitial(displayName));
    badge.addClassNames(
            LumoUtility.FontWeight.SEMIBOLD,
            LumoUtility.FontSize.SMALL,
            LumoUtility.TextAlignment.CENTER
    );

    badge.getStyle()
            .setDisplay(Style.Display.INLINE_FLEX)
            .setAlignItems(Style.AlignItems.CENTER)
            .setJustifyContent(Style.JustifyContent.CENTER)
            .setWidth("2.2rem")
            .setHeight("2.2rem")
            .setBorderRadius("9999px")
            .setCursor(Cursor.POINTER.toString())
            .setBorder("1px solid var(--lumo-contrast-20pct)")
            .setBackground("var(--lumo-contrast-5pct)")
            .set("user-select", "none");

    layout.add(badge);

    menu = new ContextMenu(badge);
    menu.setOpenOnClick(true);

    // Re-evaluate all registered enabled-suppliers every time the menu opens
    menu.addOpenedChangeListener(event -> {
      if (event.isOpened()) {
        refreshEnabledStates();
      }
    });
  }

  @Nonnull
  public ContextMenu menu() {
    return menu;
  }

  @Nonnull
  public UserActionBadge addItem(@Nonnull final String text, @Nonnull final Runnable onClick) {
    return addItem(UserBadgeMenuItem.simple(text, onClick));
  }

  @Nonnull
  public UserActionBadge addItem(@Nonnull final String text,
                                 @Nonnull final Runnable onClick,
                                 @Nullable final Supplier<Boolean> enabled) {
    final var menuItem = new UserBadgeMenuItem(null, text, onClick, enabled, null);
    return addItem(menuItem);
  }

  @Nonnull
  public UserActionBadge addItemWithTranslationKey(@Nonnull final VaadinIcon icon,
                                                   @Nonnull final String key,
                                                   @Nonnull final Runnable onClick) {
    return addItem(UserBadgeMenuItem.simple(icon, getTranslation(key), onClick));
  }

  @Nonnull
  public UserActionBadge addItemWithTranslationKey(@Nonnull final VaadinIcon icon,
                                                   @Nonnull final String key,
                                                   @Nonnull final Runnable onClick,
                                                   @Nullable final Supplier<Boolean> enabled) {
    return addItem(new UserBadgeMenuItem(icon, getTranslation(key), onClick, enabled, null));
  }

  @Nonnull
  public UserActionBadge addItem(@Nonnull final VaadinIcon icon,
                                 @Nonnull final String text,
                                 @Nonnull final Runnable onClick) {
    return addItem(UserBadgeMenuItem.simple(icon, text, onClick));
  }

  @Nonnull
  public UserActionBadge addItem(@Nonnull final UserBadgeMenuItem menuItem) {
    final var textSpan = new VASpan(menuItem.text());

    final var wrapper = new VASpan();
    wrapper.getStyle().setDisplay(Style.Display.FLEX);
    wrapper.getStyle().setAlignItems(Style.AlignItems.CENTER);
    wrapper.getStyle().setGap(VernoUtility.LUMO_SPACE_M);

    Optional.ofNullable(menuItem.icon()).ifPresent(icon -> wrapper.add(icon.create()));
    wrapper.add(textSpan);
    Optional.ofNullable(menuItem.shortcut()).ifPresent(shortcut -> wrapper.add(ShortcutDisplayUtil.createKeyBadge(shortcut)));
    registerShortcut(menuItem.shortcut(), menuItem.action());

    final var item = menu.addItem(wrapper, e -> menuItem.action().run());

    registerEnabledBinding(item, menuItem.enabled());
    return this;
  }

  @Nonnull
  public UserActionBadge addContent(@Nonnull final Component component) {
    menu.addItem(component);
    return this;
  }

  private void registerShortcut(@Nullable final VAShortcut shortcut,
                                @Nonnull final Runnable action) {
    Optional.ofNullable(shortcut).ifPresent(s -> {
      final var ui = UI.getCurrent();
      final var registration = Shortcuts.addShortcutListener(
                      this,
                      action::run,
                      s.getKey(),
                      s.getKeyModifier())
              .listenOn(ui);

      shortcutRegistry.get().register(s, action, this, registration);
    });
  }

  private void registerEnabledBinding(@Nonnull final MenuItem item,
                                      @Nullable final Supplier<Boolean> enabled) {
    if (enabled == null) {
      return;
    }

    enabledBindings.add(new EnabledBinding(item, enabled));
    item.setEnabled(enabled.get());
  }

  private void refreshEnabledStates() {
    for (final var binding : enabledBindings) {
      binding.item().setEnabled(binding.enabled().get());
    }
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

  private record EnabledBinding(@Nonnull MenuItem item,
                                @Nonnull Supplier<Boolean> enabled) {
  }
}