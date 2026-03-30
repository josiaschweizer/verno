package ch.verno.ui.base.shortcut;

import ch.verno.ui.lib.os.OS;
import ch.verno.ui.lib.os.OSUtil;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.ShortcutRegistration;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Arrays;

public class RegisterShortcutUtil {

  private RegisterShortcutUtil() {
  }

  /**
   * Registers a focus shortcut on the given focusable target.
   *
   * @param target the focusable target component the shortcut should be registered on
   * @param shortcut the shortcut definition containing key and modifiers
   * @return the created {@link ShortcutRegistration}
   */
  @Nonnull
  public static ShortcutRegistration addFocusShortcut(
          @Nonnull final Focusable<?> target,
          @Nonnull final VAShortcut shortcut
  ) {
    final KeyModifier[] keyModifiers =
            getKeyModifiersAccordingToOS(shortcut.keyModifier());

    return target.addFocusShortcut(
            shortcut.key(),
            keyModifiers
    );
  }

  @Nonnull
  private static KeyModifier[] getKeyModifiersAccordingToOS(
          @Nullable final KeyModifier... keyModifier
  ) {
    if (keyModifier == null || keyModifier.length == 0) {
      return new KeyModifier[0];
    }

    final OS os = OSUtil.getOs();

    return Arrays.stream(keyModifier)
            .map(modifier -> translateModifier(modifier, os))
            .toArray(KeyModifier[]::new);
  }

  @Nonnull
  private static KeyModifier translateModifier(
          @Nonnull final KeyModifier modifier,
          @Nonnull final OS os
  ) {
    return switch (modifier) {
      case CONTROL -> os == OS.MAC
              ? KeyModifier.META
              : KeyModifier.CONTROL;

      case META -> os != OS.MAC
              ? KeyModifier.CONTROL
              : KeyModifier.META;

      case SHIFT -> KeyModifier.SHIFT;
      case ALT -> KeyModifier.ALT;
      default -> modifier;
    };
  }
}