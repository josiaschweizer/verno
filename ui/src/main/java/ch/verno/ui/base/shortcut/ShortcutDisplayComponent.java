package ch.verno.ui.base.shortcut;

import ch.verno.publ.Publ;
import ch.verno.publ.VernoUtility;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;

public class ShortcutDisplayComponent extends HorizontalLayout {

  private ShortcutDisplayComponent(@Nonnull final VAShortcut shortcut) {
    setSpacing(VernoUtility.LUMO_SPACE_XS);
    setPadding(false);
    setMargin(false);

    final var display = ShortcutDisplayUtil.toDisplayString(shortcut);
    final var parts = display.split(Publ.SPACE + "\\+" + Publ.SPACE);

    for (final var part : parts) {
      add(createKeyChip(part));
    }
  }

  @Nonnull
  public static ShortcutDisplayComponent of(@Nonnull final VAShortcut shortcut) {
    return new ShortcutDisplayComponent(shortcut);
  }

  @Nonnull
  private Span createKeyChip(@Nonnull final String text) {
    final var chip = new Span(text);

    chip.getStyle()
            .setFontSize("1rem")
            .setPadding("0.125rem 0.375rem")
            .setBorderRadius("1px")
            .setBackground("var(--lumo-contrast-10pct)")
            .set("font-family", "monospace");

    return chip;
  }
}