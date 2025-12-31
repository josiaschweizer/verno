package ch.verno.ui.base.components.toggle;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import jakarta.annotation.Nonnull;

@CssImport("./components/toggle/va-toggle-field.css")
public class VAToggleField extends AbstractField<VAToggleField, Boolean> implements HasSize {

  private final Span left;
  private final Span right;

  public VAToggleField(@Nonnull final String leftLabel,
                       @Nonnull final String rightLabel) {
    super(false);

    final var root = new Div();
    this.left = new Span(leftLabel);
    this.right = new Span(rightLabel);

    root.addClassName("va-toggle");
    left.addClassName("va-toggle__option");
    right.addClassName("va-toggle__option");

    root.add(left, right);

    getElement().appendChild(root.getElement());

    left.getElement().addEventListener("click", e -> setValue(false));
    right.getElement().addEventListener("click", e -> setValue(true));

    updateUi();
  }

  @Override
  protected void setPresentationValue(final Boolean value) {
    updateUi();
  }

  private void updateUi() {
    final var value = Boolean.TRUE.equals(getValue());
    left.getElement().getClassList().set("is-active", !value);
    right.getElement().getClassList().set("is-active", value);
  }
}