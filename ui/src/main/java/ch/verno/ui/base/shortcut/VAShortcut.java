package ch.verno.ui.base.shortcut;

import ch.verno.ui.i18n.TranslationHelper;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface VAShortcut {

  @Nonnull
  Key getKey();

  @Nullable
  KeyModifier[] getKeyModifier();

  @Nonnull
  String caption(@Nonnull TranslationHelper helper);

  default boolean browserDefaultAllowed() {
    return false;
  }

}
