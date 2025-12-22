package ch.verno.ui.base.components.entry.twooption;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.function.Function;

@CssImport("./components/two-option-entry.css")
public class TwoOptionEntry<T> extends CustomField<T> {

  @Nonnull
  private final Button leftButton;
  @Nonnull
  private final Button rightButton;

  @Nonnull
  private final T leftValue;
  @Nonnull
  private final T rightValue;

  @Nullable
  private T currentValue;

  public TwoOptionEntry(@Nonnull final String label,
                        @Nonnull final List<T> options,
                        @Nonnull final Function<T, String> itemLabel) {
    if (options.size() != 2) {
      throw new IllegalArgumentException("TwoOptionEntry requires exactly 2 options, but got: " + options.size());
    }

    this.leftValue = options.get(0);
    this.rightValue = options.get(1);

    setLabel(label);

    leftButton = new Button();
    leftButton.setText(itemLabel.apply(leftValue));
    rightButton = new Button();
    rightButton.setText(itemLabel.apply(rightValue));

    final var layout = new HorizontalLayout();
    layout.setWidthFull();
    layout.setPadding(false);
    layout.setSpacing(true);

    layout.setFlexGrow(1, leftButton);
    layout.setFlexGrow(1, rightButton);

    leftButton.addClickListener(event -> setValue(leftValue));
    rightButton.addClickListener(event -> setValue(rightValue));

    layout.add(leftButton, rightButton);
    add(layout);

    updateSelectionStyles(null);
  }

  @Nullable
  @Override
  protected T generateModelValue() {
    return currentValue;
  }

  @Override
  protected void setPresentationValue(@Nullable final T value) {
    currentValue = value;
    updateSelectionStyles(value);
  }

  private void updateSelectionStyles(T value) {
    leftButton.getElement().getClassList().set("two-option-selected",
        value != null && value.equals(leftValue));
    rightButton.getElement().getClassList().set("two-option-selected",
        value != null && value.equals(rightValue));
  }
}