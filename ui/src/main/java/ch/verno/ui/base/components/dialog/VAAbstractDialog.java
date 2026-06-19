package ch.verno.ui.base.components.dialog;

import ch.verno.lib.VernoUtility;
import ch.verno.ui.base.components.button.VAButton;
import ch.verno.ui.lib.icon.CustomIcons;
import ch.verno.ui.lib.icon.IconUtil;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Collection;
import java.util.function.Consumer;

public abstract class VAAbstractDialog extends Dialog {

  protected void initUI(@Nonnull final String title) {
    initUI(title, DialogSize.BIG);
  }

  protected void initUI(@Nullable final String title,
                        @Nonnull final DialogSize dialogSize) {
    setHeight("auto");
    setMaxHeight(dialogSize.getMaxHeight());
    setMinHeight(dialogSize.getMinHeight());

    setWidth(dialogSize.getWidth());
    setMinWidth(dialogSize.getMinWidth());
    setMaxWidth("95vw");

    final var footerLayout = new HorizontalLayout();
    footerLayout.setPadding(false);
    footerLayout.setSpacing(true);
    footerLayout.getStyle().setGap(VernoUtility.LUMO_SPACE_M);

    createActionButtons().forEach(footerLayout::add);

    if (title != null) {
      setHeaderTitle(title);
    }

    add(createContent());
    getFooter().add(footerLayout);
  }

  @Nonnull
  protected abstract HorizontalLayout createContent();

  @Nonnull
  protected abstract Collection<Button> createActionButtons();

  @Nonnull
  protected VAButton createCancelButton() {
    final var button = new VAButton(getTranslation("shared.cancel"));
    button.addClickListener(e -> close());
    return button;
  }

  @Nonnull
  protected VAButton createSaveButton(@Nonnull final Consumer<ClickEvent<Button>> action) {
    final var button = new VAButton(getTranslation("shared.save"), IconUtil.creatExtraSmall(CustomIcons.SAVE));
    button.addClickListener(action::accept);
    return button;
  }

}