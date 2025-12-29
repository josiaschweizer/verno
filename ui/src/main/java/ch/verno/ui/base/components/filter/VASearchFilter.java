package ch.verno.ui.base.components.filter;

import ch.verno.ui.base.components.entry.textfield.VATextField;
import com.vaadin.flow.component.customfield.CustomField;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class VASearchFilter extends CustomField<String> {

  @Nonnull
  private final VATextField textField;

  @Nullable
  private String currentValue;

  public VASearchFilter() {
    this(null, null);
  }

  public VASearchFilter(@Nullable final String placeholder) {
    this(null, placeholder);
  }

  public VASearchFilter(@Nullable final String label,
                        @Nullable final String placeholder) {
    setWidthFull();

    textField = new VATextField();
    textField.setWidthFull();

    if (label != null) {
      textField.setLabel(label);
    }

    if (placeholder != null) {
      textField.setPlaceholder(placeholder);
    } else {
      textField.setPlaceholder("Search...");
    }

    textField.setClearButtonVisible(true);

    textField.addValueChangeListener(event -> {
      final var newValue = event.getValue();
      if (newValue == null || newValue.isEmpty()) {
        currentValue = null;
      } else {
        currentValue = newValue;
      }
      setValue(currentValue);
    });

    add(textField);
  }

  public void setFilterWidth(@Nonnull final String width) {
    textField.setWidth(width);
  }

  public void setMaxLength(final int maxLength) {
    textField.setMaxLength(maxLength);
  }

  @Nonnull
  public VATextField getTextField() {
    return textField;
  }

  @Nullable
  public String getFilterValue() {
    return currentValue;
  }

  public boolean isEmpty() {
    return currentValue == null || currentValue.isEmpty();
  }

  @Nullable
  @Override
  protected String generateModelValue() {
    return currentValue;
  }

  @Override
  protected void setPresentationValue(@Nullable final String value) {
    currentValue = value;
    if (value == null || value.isEmpty()) {
      textField.setValue("");
    } else {
      textField.setValue(value);
    }
  }

  @Override
  public void setEnabled(final boolean enabled) {
    super.setEnabled(enabled);
    textField.setEnabled(enabled);
  }
}

