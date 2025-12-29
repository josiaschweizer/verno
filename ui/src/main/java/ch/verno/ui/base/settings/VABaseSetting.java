package ch.verno.ui.base.settings;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@CssImport("./components/va-base-setting.css")
public abstract class VABaseSetting extends Div {

  @Nonnull
  private final Div headerWrapper;
  @Nonnull
  private final Div contentWrapper;
  @Nullable
  protected Component contentComponent;
  @Nullable
  private Span actionButtonSpan;

  protected VABaseSetting(@Nonnull final String title) {
    addClassName("setting-card");

    headerWrapper = new Div();
    headerWrapper.addClassName("setting-card-header");

    final var titleSpan = new Span(title);
    titleSpan.addClassName("setting-card-title");

    headerWrapper.add(titleSpan);
    add(headerWrapper);

    contentWrapper = new Div();
    contentWrapper.addClassName("setting-card-content");
    add(contentWrapper);
  }

  protected final void setActionButton(@Nonnull final Button actionButton) {
    if (actionButtonSpan != null) {
      headerWrapper.remove(actionButtonSpan);
      actionButtonSpan = null;
    }

    actionButtonSpan = new Span(actionButton);
    actionButtonSpan.addClassName("setting-card-action-button");
    headerWrapper.add(actionButtonSpan);
  }

  @Override
  protected void onAttach(@Nonnull final AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    if (contentComponent == null) {
      setContent(createContent());
    }
  }

  protected final void setContent(@Nonnull final Component newContent) {
    if (contentComponent != null) {
      contentWrapper.remove(contentComponent);
    }

    contentComponent = newContent;
    contentWrapper.add(contentComponent);
  }

  @Nonnull
  protected abstract Component createContent();
}