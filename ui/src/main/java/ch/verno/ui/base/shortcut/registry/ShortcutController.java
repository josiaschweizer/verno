package ch.verno.ui.base.shortcut.registry;

import ch.verno.ui.base.shortcut.VAShortcut;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public interface ShortcutController {

  void register(@Nonnull VAShortcut shortcut,
                @Nonnull ShortcutRegistryEntry entry);

  void register(@Nonnull VAShortcut shortcut,
                @Nonnull Runnable action,
                @Nonnull Component owner);

  void register(@Nonnull VAShortcut shortcut,
                @Nonnull Runnable action,
                @Nonnull Component owner,
                @Nullable Registration registration);

  @Nonnull
  List<VAShortcut> getShortcuts();

  @Nonnull
  ShortcutRegistryEntry getEntryByShortcut(@Nonnull VAShortcut shortcut);

  boolean isRegistryEmpty();

}
