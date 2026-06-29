package ch.verno.ui.base.shortcut.registry;

import ch.verno.lib.New;
import ch.verno.ui.base.shortcut.VAShortcut;
import ch.verno.ui.injection.scope.PageScoped;
import com.google.inject.Inject;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;

@PageScoped
public class ShortcutControllerImpl implements ShortcutController {

  @Nonnull private final Map<VAShortcut, ShortcutRegistryEntry> registry;

  @Inject
  public ShortcutControllerImpl() {
    this.registry = New.concurrentHashMap();
  }

  @Override
  public void register(@Nonnull final VAShortcut shortcut,
                       @Nonnull final ShortcutRegistryEntry entry) {
    if (registry.containsKey(shortcut)) {
      throw new IllegalStateException("Shortcut already registered: " + shortcut);
    }

    registry.put(shortcut, entry);
  }

  @Override
  public void register(@Nonnull final VAShortcut shortcut,
                       @Nonnull final Runnable action,
                       @Nonnull final Component owner) {
    final var registryEntry = ShortcutRegistryEntry.simple(action, owner);
    registry.put(shortcut, registryEntry);
  }

  @Override
  public void register(@Nonnull final VAShortcut shortcut,
                       @Nonnull final Runnable action,
                       @Nonnull final Component owner,
                       @Nullable final Registration registration) {
    final var registryEntry = new ShortcutRegistryEntry(action, owner, registration);
    registry.put(shortcut, registryEntry);
  }

  @Nonnull
  @Override
  public List<VAShortcut> getShortcuts() {
    return New.list(registry.keySet());
  }

  @Nonnull
  @Override
  public ShortcutRegistryEntry getEntryByShortcut(@Nonnull VAShortcut shortcut) {
    return registry.get(shortcut);
  }

  @Override
  public boolean isRegistryEmpty() {
    return registry.isEmpty();
  }
}
