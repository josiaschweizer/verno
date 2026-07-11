package ch.verno.ui.base.shortcut.registry;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record ShortcutRegistryEntry(@Nonnull Runnable action,
                                    @Nonnull Component owner,
                                    @Nullable Registration registration) {

  @Nonnull
  public static ShortcutRegistryEntry simple(@Nonnull final Runnable action,
                                             @Nonnull final Component owner) {
    return new ShortcutRegistryEntry(action, owner, null);
  }

}