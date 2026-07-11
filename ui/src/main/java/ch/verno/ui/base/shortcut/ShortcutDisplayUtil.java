package ch.verno.ui.base.shortcut;

import ch.verno.lib.Publ;
import ch.verno.lib.VernoUtility;
import ch.verno.lib.font.Font;
import ch.verno.ui.base.components.div.VADiv;
import ch.verno.ui.base.os.OS;
import ch.verno.ui.base.os.OSUtil;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ShortcutDisplayUtil {

  @Nonnull
  public static String toDisplayString(@Nonnull final VAShortcut shortcut) {
    final var os = OSUtil.getOs();

    final var modifiers = shortcut.getKeyModifier() != null ?
            Arrays.stream(shortcut.getKeyModifier())
                    .map(mod -> mapModifierToDisplay(mod, os))
                    .collect(Collectors.joining(" + "))
            : Publ.EMPTY_STRING;

    final var key = mapKeyToDisplay(shortcut.getKey());

    if (modifiers.isBlank()) {
      return key;
    }

    return modifiers + " + " + key;
  }

  @Nonnull
  public static VADiv createKeyBadge(@Nonnull final VAShortcut shortcut) {
    final var badge = new VADiv(toDisplayString(shortcut));
    badge.addClassNames(
            LumoUtility.FontSize.SMALL,
            LumoUtility.Background.CONTRAST_5,
            LumoUtility.BorderColor.CONTRAST_20,
            LumoUtility.Border.ALL,
            LumoUtility.BorderRadius.SMALL,
            LumoUtility.Padding.Horizontal.SMALL,
            LumoUtility.Padding.Vertical.XSMALL
    );
    badge.getStyle().setMinWidth(VernoUtility.FOUR_REM);
    badge.getStyle().setTextAlign(Style.TextAlign.CENTER);
    badge.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
    badge.getStyle().set("font-family", Font.MONOSPACE_FONT_STACK);

    return badge;
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
      case "Comma" -> Publ.COMMA;
      case "Period" -> Publ.DOT;
      case "Semicolon" -> Publ.SEMICOLON;
      case "Slash" -> Publ.SLASH;
      case "Backslash" -> Publ.BACKSLASH;
      case "Minus" -> Publ.MINUS;
      case "Equal" -> Publ.EQUALS;
      case "BracketLeft" -> Publ.BRACKET_LEFT;
      case "BracketRight" -> Publ.BRACKET_RIGHT;
      case "Quote" -> Publ.SIMPLE_QUOTE;
      case "Backquote" -> Publ.BACKQUOTE;
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
}