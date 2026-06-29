package ch.verno.ui.base.shortcut.dialog.content;

import ch.verno.ui.base.components.div.VADiv;
import ch.verno.ui.base.components.layout.horizontal.VAHorizontalLayout;
import ch.verno.ui.base.shortcut.ShortcutDisplayUtil;
import ch.verno.ui.base.shortcut.VAShortcut;
import ch.verno.ui.i18n.TranslationHelper;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;

public class ShortcutEntry extends VAHorizontalLayout {

  @Nonnull private final VAShortcut shortcut;
  private final TranslationHelper translationHelper;

  public ShortcutEntry(@Nonnull final VAShortcut shortcut,
                       @Nonnull final TranslationHelper translationHelper) {
    this.shortcut = shortcut;
    this.translationHelper = translationHelper;

    initUI();
  }

  private void initUI() {
    setAlignItems(FlexComponent.Alignment.CENTER);
    setSpacing(false);
    addClassNames(LumoUtility.Gap.MEDIUM, LumoUtility.Padding.Vertical.SMALL);

    final var keyBadge = ShortcutDisplayUtil.createKeyBadge(shortcut);
    final var captionDiv = new VADiv(shortcut.caption(translationHelper));
    captionDiv.addClassNames(LumoUtility.FontSize.SMALL);

    add(keyBadge, captionDiv);
  }
}