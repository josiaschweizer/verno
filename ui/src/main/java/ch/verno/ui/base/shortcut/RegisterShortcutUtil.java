package ch.verno.ui.base.shortcut;

import ch.verno.ui.lib.os.OS;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.ShortcutRegistration;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Arrays;

public class RegisterShortcutUtil {

  private RegisterShortcutUtil() {
  }

  @Nonnull
  public static ShortcutRegistration addFocusShortcut(@Nonnull final Focusable<?> target,
                                                      @Nonnull final VAShortcut shortcut) {
    final var keyModifiers = getKeyModifiersAccordingToOS(shortcut.keyModifier());
    return target.addFocusShortcut(
            shortcut.key(),
            keyModifiers
    );
  }

  @Nonnull
  private static KeyModifier[] getKeyModifiersAccordingToOS(@Nullable final KeyModifier... keyModifier) {
    if (keyModifier == null || keyModifier.length == 0) {
      return new KeyModifier[0];
    }

    final var os = getOS();

    return Arrays.stream(keyModifier)
            .map(modifier -> translateModifier(modifier, os))
            .toArray(KeyModifier[]::new);
  }

  @Nonnull
  private static OS getOS() {
    final var osString = System.getProperty("os.name");
    return OS.getFromKey(osString);
  }

  @Nonnull
  private static KeyModifier translateModifier(@Nonnull final KeyModifier modifier,
                                               @Nonnull final OS os) {
    return switch (modifier) {
      case CONTROL -> {
        if (os == OS.MAC) {
          yield KeyModifier.META;
        }
        yield KeyModifier.CONTROL;
      }

      case META -> {
        if (os != OS.MAC) {
          yield KeyModifier.CONTROL;
        }
        yield KeyModifier.META;
      }

      case SHIFT -> KeyModifier.SHIFT;
      case ALT -> KeyModifier.ALT;

      default -> modifier;
    };
  }

}
