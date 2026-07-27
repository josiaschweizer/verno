package ch.verno.ui.base.shortcut;

import ch.verno.ui.base.os.OS;
import ch.verno.ui.base.os.OSUtil;
import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.ShortcutRegistration;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Arrays;

public class ShortcutRegistrationUtil {

  private ShortcutRegistrationUtil() {
  }

  /**
   * Registers a focus shortcut on the given focusable target.
   *
   * @param target the focusable target component the shortcut should be registered on
   * @param shortcut the shortcut definition containing key and modifiers
   * @return the created {@link ShortcutRegistration}
   */
  @Nonnull
  public static ShortcutRegistration addFocusShortcut(@Nonnull final Focusable<?> target,
                                                      @Nonnull final VAShortcut shortcut) {
    final var keyModifiers = getKeyModifiersAccordingToOS(shortcut.getKeyModifier());

    final var registration = target.addFocusShortcut(shortcut.getKey(), keyModifiers);
    registration.setBrowserDefaultAllowed(shortcut.browserDefaultAllowed());
    return registration;
  }

  /**
   * Registers a click shortcut on the given target which implements {@code ClickNotifier}
   *
   * @param target the target component which implements {@code ClickNotifier} on which the shortcut should be registered on
   * @param shortcut the shortcut definition containing key, modifiers & boolean whether the browser default is allowed
   * @return te created {@link ShortcutRegistration}
   */
  @Nonnull
  public static ShortcutRegistration addClickShortcut(@Nonnull final ClickNotifier<?> target,
                                                      @Nonnull final VAShortcut shortcut) {
    final var keyModifiers = getKeyModifiersAccordingToOS(shortcut.getKeyModifier());

    final var registration = target.addClickShortcut(shortcut.getKey(), keyModifiers);
    registration.setBrowserDefaultAllowed(shortcut.browserDefaultAllowed());
    return registration;
  }

  @Nonnull
  private static KeyModifier[] getKeyModifiersAccordingToOS(@Nullable final KeyModifier... keyModifier) {
    if (keyModifier == null || keyModifier.length == 0) {
      return new KeyModifier[0];
    }

    final var os = OSUtil.getOs();
    return Arrays.stream(keyModifier)
            .map(modifier -> translateModifier(modifier, os))
            .toArray(KeyModifier[]::new);
  }

  @Nonnull
  private static KeyModifier translateModifier(@Nonnull final KeyModifier modifier,
                                               @Nonnull final OS os) {
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