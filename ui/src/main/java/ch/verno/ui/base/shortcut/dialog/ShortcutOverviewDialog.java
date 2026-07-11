package ch.verno.ui.base.shortcut.dialog;

import ch.verno.lib.New;
import ch.verno.ui.base.components.dialog.DialogSize;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import ch.verno.ui.base.components.layout.horizontal.VAHorizontalLayout;
import ch.verno.ui.base.shortcut.dialog.content.ShortcutDialogContent;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.button.Button;
import jakarta.annotation.Nonnull;

import java.util.Collection;

public class ShortcutOverviewDialog extends VAAbstractDialog {

  @Nonnull private final Injector injector;

  @Inject
  public ShortcutOverviewDialog(@Nonnull final Injector injector) {
    this.injector = injector;

    initUI(getTranslation("shared.shortcuts"), DialogSize.SMALL_COMPACT);
  }

  @Nonnull
  @Override
  protected VAHorizontalLayout createContent() {
    return new VAHorizontalLayout(injector.getInstance(ShortcutDialogContent.class));
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    return New.list(createCloseButton());
  }
}
