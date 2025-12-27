package ch.verno.ui.base.settings;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import jakarta.annotation.Nonnull;

public abstract class BaseSetting extends Div {

  protected BaseSetting(@Nonnull final String title) {
    addClassName("setting-card");

    final var header = createHeader(title);
    final var content = createContent();

    add(header, content);
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