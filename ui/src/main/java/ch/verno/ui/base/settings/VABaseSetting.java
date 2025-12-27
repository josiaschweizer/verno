package ch.verno.ui.base.settings;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import jakarta.annotation.Nonnull;

@CssImport("./components/va-base-setting.css")
public abstract class VABaseSetting extends Div {

  private Component contentComponent;

  protected VABaseSetting(@Nonnull final String title) {
    addClassName("setting-card");

    final var header = createHeader(title);
    add(header);
  }

  @Override
  protected void onAttach(@Nonnull final AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    if (contentComponent == null) {
      contentComponent = createContent();
    }

    // Only add the content if it's not already attached somewhere
    // This prevents duplicate children when navigating away and back.
    if (!contentComponent.isAttached()) {
      add(contentComponent);
    }
  }

  @Nonnull
  protected abstract Component createContent();

  @Nonnull
  private Component createHeader(@Nonnull final String title) {
    final var wrapper = new Div();
    wrapper.addClassName("setting-card-header");

    final var titleSpan = new Span(title);
    titleSpan.addClassName("setting-card-title");

    wrapper.add(titleSpan);
    return wrapper;
  }
}