package ch.verno.ui.base.dialog;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

    setWidth(dialogSize.getWidth());
    setMinWidth(dialogSize.getMinWidth());
    setMaxWidth("95vw");

    setHeaderTitle(title);
    add(createContent());
    createActionButtons().forEach(btn -> getFooter().add(btn));
  }

  @Nonnull
  protected abstract HorizontalLayout createContent();

  @Nonnull
  protected abstract Collection<Button> createActionButtons();

  @Nonnull
  protected VerticalLayout createVerticalLayoutFromComponents(@Nonnull final Component... components) {
    final var layout = new VerticalLayout(components);
    layout.setPadding(false);
    layout.setSpacing(false);
    layout.setWidthFull();
    return layout;
  }

  @Nonnull
  public HorizontalLayout createHorizontalLayoutFromComponents(@Nonnull final Component... components) {
    final var layout = new HorizontalLayout();
    layout.setWidthFull();

    layout.getStyle().set("flex-wrap", "wrap");
    layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.START);

    for (final var component : components) {
      component.getElement().getStyle().set("min-width", "260px");
      component.getElement().getStyle().set("flex", "1 1 260px");
      layout.add(component);
    }

    return layout;
  }
}