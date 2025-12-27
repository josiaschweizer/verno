package ch.verno.ui.base.components.entry.textfield;

import ch.verno.common.util.Publ;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.textfield.TextField;
import jakarta.annotation.Nonnull;

public class VATextField extends TextField {

  public VATextField() {
    super(Publ.EMPTY_STRING);
    addClassName("va-text-field");
  }

  public VATextField(@Nonnull final String label) {
    this();
    this.setLabel(label);
  }

  public VATextField(@Nonnull final String label,
                     @Nonnull final String placeholder) {
    this(label);
    this.setPlaceholder(placeholder);
  }

  public VATextField(@Nonnull final String label,
                     @Nonnull final String initialValue,
                     @Nonnull final String placeholder) {
    this(label);
    this.setValue(initialValue);
    this.setPlaceholder(placeholder);
  }

  public VATextField(@Nonnull final ValueChangeListener<? super ComponentValueChangeEvent<TextField, String>> listener) {
    this();
    this.addValueChangeListener(listener);
  }

  public VATextField(@Nonnull final String label,
                     @Nonnull final ValueChangeListener<? super ComponentValueChangeEvent<TextField, String>> listener) {
    this(label);
    this.addValueChangeListener(listener);
  }

  public VATextField(@Nonnull final String label,
                     @Nonnull final String initialValue, HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<TextField, String>> listener) {
    this(label);
    this.setValue(initialValue);
    this.addValueChangeListener(listener);
  }
}
