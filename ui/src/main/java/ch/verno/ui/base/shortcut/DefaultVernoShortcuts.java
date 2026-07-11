package ch.verno.ui.base.shortcut;

import ch.verno.ui.i18n.TranslationHelper;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public enum DefaultVernoShortcuts implements VAShortcut {
  SAVE("common.save", Key.KEY_S, KeyModifier.ALT),
  FOCUS("common.focus", Key.KEY_F, KeyModifier.ALT),
  SHORTCUTS("shared.shortcuts", Key.COMMA, KeyModifier.ALT),
  ;

  @Nonnull private final String translationKey;
  @Nonnull private final Key key;
  @Nullable private final KeyModifier[] keyModifier;

  DefaultVernoShortcuts(@Nonnull final String translationKey,
                        @Nonnull Key key,
                        @Nullable final KeyModifier... keyModifier) {
    this.translationKey = translationKey;
    this.key = key;
    this.keyModifier = keyModifier;
  }


  @Nonnull
  @Override
  public Key getKey() {
    return key;
  }

  @Nullable
  @Override
  public KeyModifier[] getKeyModifier() {
    return keyModifier;
  }

  @Nonnull
  public String getTranslationKey() {
    return translationKey;
  }

  @Nonnull
  @Override
  public String caption(@Nonnull final TranslationHelper helper) {
    return helper.getTranslation(getTranslationKey());
  }

}
