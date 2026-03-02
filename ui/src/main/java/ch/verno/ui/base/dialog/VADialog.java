package ch.verno.ui.base.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;

import java.util.Collection;

public abstract class VADialog extends Dialog {

  protected void initUI(@Nonnull final String title) {
    initUI(title, DialogSize.BIG);
  }

  protected void initUI(@Nonnull final String title,
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
    footerLayout.getStyle().setGap("var(--lumo-space-m)");

    createActionButtons().forEach(footerLayout::add);

    setHeaderTitle(title);
    add(createContent());
    getFooter().add(footerLayout);
  }

  @Nonnull
  protected abstract HorizontalLayout createContent();

  @Nonnull
  protected abstract Collection<Button> createActionButtons();

}