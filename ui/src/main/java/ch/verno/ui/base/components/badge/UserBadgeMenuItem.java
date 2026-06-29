package ch.verno.ui.base.components.badge;

import ch.verno.ui.base.shortcut.VAShortcut;
import com.vaadin.flow.component.icon.VaadinIcon;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.function.Supplier;

public record UserBadgeMenuItem(@Nullable VaadinIcon icon,
                                @Nonnull String text,
                                @Nonnull Runnable action,
                                @Nullable Supplier<Boolean> enabled,
                                @Nullable VAShortcut shortcut) {

  public static UserBadgeMenuItem simple(@Nonnull final String text,
                                         @Nonnull final Runnable onClick) {
    return new UserBadgeMenuItem(null, text, onClick, null, null);
  }

  @Nonnull
  public static UserBadgeMenuItem simple(@Nonnull final VaadinIcon icon,
                                         @Nonnull final String text,
                                         @Nonnull final Runnable onClick) {
    return new UserBadgeMenuItem(icon, text, onClick, null, null);
  }
}
