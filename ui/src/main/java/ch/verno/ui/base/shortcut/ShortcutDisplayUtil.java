package ch.verno.ui.base.shortcut;

import ch.verno.publ.Publ;
import ch.verno.ui.lib.os.OS;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ShortcutDisplayUtil {

  @Nonnull
  public static String toDisplayString(@Nonnull final VAShortcut shortcut) {
    final var os = getOS();
    final var modifiers = Arrays.stream(shortcut.keyModifier())
            .map(mod -> mapModifierToDisplay(mod, os))
            .collect(Collectors.joining(" + "));

    final var key = mapKeyToDisplay(shortcut.key());

    if (modifiers.isEmpty()) {
      return key;
    }

    return modifiers + " + " + key;
  }

  @Nonnull
  private static String mapModifierToDisplay(@Nonnull final KeyModifier modifier,
                                             @Nonnull final OS os) {
    return switch (modifier) {
      case META -> os == OS.MAC ? Publ.CMD_SIGN : "Meta";
      case CONTROL -> os == OS.MAC ? Publ.CTRL_MAC_SIGN : "Ctrl";

      case SHIFT -> Publ.SHIFT_SIGN;
      case ALT -> os == OS.MAC ? Publ.OPTION_OPTION : "Alt";

      default -> modifier.name();
    };
  }

  @Nonnull
  private static String mapKeyToDisplay(@Nonnull final Key key) {
    final var raw = key.getKeys().isEmpty()
            ? Publ.EMPTY_STRING
            : key.getKeys().getFirst();

    return switch (raw) {
      case Publ.SPACE -> "Space";
      case "ArrowUp" -> Publ.ARROW_UP;
      case "ArrowDown" -> Publ.ARROW_DOWN;
      case "ArrowLeft" -> Publ.ARROW_LEFT;
      case "ArrowRight" -> Publ.ARROW_RIGHT;
      case "Escape" -> "Esc";
      case "Enter" -> "Enter";
      case "Tab" -> "Tab";
      default -> {
        if (raw.startsWith("Key")) {
          yield raw.replace("Key", Publ.EMPTY_STRING);
        }

        yield raw.length() == 1
                ? raw.toUpperCase()
                : raw;
      }
    };
  }

  @Nonnull
  private static OS getOS() {
    final var osString = System.getProperty("os.name", Publ.EMPTY_STRING);
    return OS.getFromKey(osString);
  }
}