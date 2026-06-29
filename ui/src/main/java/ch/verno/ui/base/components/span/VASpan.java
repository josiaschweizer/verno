package ch.verno.ui.base.components.span;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import jakarta.annotation.Nonnull;

public class VASpan extends Span {

  public VASpan() {
    super();
  }

  public VASpan(@Nonnull final Component... components) {
    super(components);
  }

  public VASpan(@Nonnull final String text) {
    super(text);
  }

}
