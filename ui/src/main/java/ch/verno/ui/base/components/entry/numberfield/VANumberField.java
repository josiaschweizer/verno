package ch.verno.ui.base.components.entry.numberfield;

import com.vaadin.flow.component.textfield.NumberField;
import jakarta.annotation.Nonnull;

public class VANumberField extends NumberField {

  public VANumberField() {
    super();
    addClassName("va-number-field");
  }

  public VANumberField(@Nonnull final String label) {
    super(label);
    addClassName("va-number-field");
  }

  public VANumberField(@Nonnull final String label,
                       @Nonnull final String placeholder) {
    super(label, placeholder);
    addClassName("va-number-field");
  }

  public VANumberField(@Nonnull final ValueChangeListener<? super ComponentValueChangeEvent<NumberField, Double>> listener) {
    super(listener);
    addClassName("va-number-field");
  }

  public VANumberField(@Nonnull final String label,
                       @Nonnull final ValueChangeListener<? super ComponentValueChangeEvent<NumberField, Double>> listener) {
    super(label, listener);
    addClassName("va-number-field");
  }

  public VANumberField(@Nonnull final String label,
                       @Nonnull final Double initialValue,
                       @Nonnull final ValueChangeListener<? super ComponentValueChangeEvent<NumberField, Double>> listener) {
    super(label, initialValue, listener);
    addClassName("va-number-field");
  }

}
