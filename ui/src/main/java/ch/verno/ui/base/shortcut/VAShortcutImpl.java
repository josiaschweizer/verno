package ch.verno.ui.base.shortcut;

import ch.verno.ui.i18n.TranslationHelper;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Arrays;
import java.util.Objects;

public record VAShortcutImpl(@Nonnull String captionKey,
                             boolean browserDefaultAllowed,
                             @Nonnull Key key,
                             @Nullable KeyModifier... keyModifier) implements VAShortcut {

  @Nonnull
  @Override
  public Key getKey() {
    return key();
  }

  @Nullable
  @Override
  public KeyModifier[] getKeyModifier() {
    return keyModifier();
  }

  @Nonnull
  @Override
  public String caption(@Nonnull final TranslationHelper helper) {
    return helper.getTranslation(captionKey());
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof VAShortcutImpl(String c, boolean browserDefault, Key k, KeyModifier[] modifier))) {
      return false;
    }

    return Objects.equals(captionKey, c) &&
            Objects.equals(key, k) &&
            Arrays.equals(keyModifier, modifier) &&
            Objects.equals(browserDefaultAllowed, browserDefault);
  }

  @Override
  public int hashCode() {
    return Objects.hash(captionKey, browserDefaultAllowed, key, Arrays.hashCode(keyModifier));
  }
}
