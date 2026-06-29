package ch.verno.ui.base.shortcut.dialog.content;

import ch.verno.ui.base.components.layout.vertical.VAVerticalLayout;
import ch.verno.ui.base.shortcut.registry.ShortcutRegistry;
import ch.verno.ui.i18n.TranslationHelper;
import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;

public class ShortcutDialogContent extends VAVerticalLayout {

  @Nonnull private final ShortcutRegistry shortcutRegistry;
  @Nonnull private final TranslationHelper translationHelper;

  @Inject
  public ShortcutDialogContent(@Nonnull final Injector injector) {
    this.translationHelper = injector.getInstance(TranslationHelper.class);
    this.shortcutRegistry = injector.getInstance(ShortcutRegistry.class);

    initUI();
  }

  private void initUI() {
    setSpacing(false);
    setPadding(false);

    final var shortcuts = shortcutRegistry.getShortcuts();
    for (final var shortcut : shortcuts) {
      final var entry = new ShortcutEntry(shortcut, translationHelper);
      add(entry);
    }

  }

}
