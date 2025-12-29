package ch.verno.ui.base.components.entry.twooption;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

@CssImport("./components/va-two-option-entry.css")
public class VATwoOptionEntry<T> extends CustomField<T> {

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

  public VATwoOptionEntry(@Nonnull final String label,
                          @Nonnull final List<T> options,
                          @Nonnull final ValueProvider<T, String> itemLabelProvider) {
    this.leftValue = options.get(0);
    this.rightValue = options.get(1);

    setLabel(label);

    leftButton = new Button(itemLabelProvider.apply(leftValue));
    rightButton = new Button(itemLabelProvider.apply(rightValue));

    final var layout = new HorizontalLayout(leftButton, rightButton);
    layout.setWidthFull();
    layout.setPadding(false);
    layout.setSpacing(true);

    layout.setFlexGrow(1, leftButton);
    layout.setFlexGrow(1, rightButton);

    leftButton.addClickListener(event -> setValue(leftValue));
    rightButton.addClickListener(event -> setValue(rightValue));

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

  private void updateSelectionStyles(@Nullable final T value) {
    leftButton.getElement().getClassList().set("two-option-selected",
        value != null && value.equals(leftValue));
    rightButton.getElement().getClassList().set("two-option-selected",
        value != null && value.equals(rightValue));
  }

  @Override
  public void setEnabled(final boolean enabled) {
    rightButton.setEnabled(enabled);
    leftButton.setEnabled(enabled);
  }

  @Override
  public void setReadOnly(final boolean readOnly) {
    rightButton.setEnabled(!readOnly);
    leftButton.setEnabled(!readOnly);
  }
}