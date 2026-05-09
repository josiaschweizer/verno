package ch.verno.ui.base.components.entry.twooption;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.ui.base.components.button.VAButton;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class VATwoOptionEntry<T> extends CustomField<T> {

  @Nonnull
  private final VAButton leftButton;
  @Nonnull
  private final VAButton rightButton;

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

    leftButton = new VAButton(itemLabelProvider.apply(leftValue));
    rightButton = new VAButton(itemLabelProvider.apply(rightValue));

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
    final boolean leftSelected = value != null && isValueEqual(value, leftValue);
    final boolean rightSelected = value != null && isValueEqual(value, rightValue);


    updateButtonStyle(leftButton, leftSelected);
    updateButtonStyle(rightButton, rightSelected);
  }

  private void updateButtonStyle(@Nonnull final VAButton button,
                                 final boolean selected) {
    if (selected) {
//      button.removeThemeVariants(ButtonVariant.LUMO_);
      button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    } else {
      button.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
//      button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    }
  }

  private boolean isValueEqual(@Nonnull final T value1,
                               @Nonnull final T value2) {
    if (value1 instanceof BaseDto && value2 instanceof BaseDto) {
      final var id1 = ((BaseDto) value1).getId();
      final var id2 = ((BaseDto) value2).getId();
      return Objects.equals(id1, id2) && id1 != null;
    }
    return value1.equals(value2);
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