package ch.verno.ui.base.components.entry.numberfield;

import com.vaadin.flow.component.textfield.NumberField;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

public class VANumberField extends NumberField {

  @NonNls public static final String VA_NUMBER_FIELD_CLASSNAME = "va-number-field";

  public VANumberField() {
    super();
    addClassName(VA_NUMBER_FIELD_CLASSNAME);
  }

  public VANumberField(@Nonnull final String label) {
    super(label);
    addClassName(VA_NUMBER_FIELD_CLASSNAME);
  }

  public VANumberField(@Nonnull final String label,
                       @Nonnull final String placeholder) {
    super(label, placeholder);
    addClassName(VA_NUMBER_FIELD_CLASSNAME);
  }

  public VANumberField(@Nonnull final ValueChangeListener<? super ComponentValueChangeEvent<NumberField, Double>> listener) {
    super(listener);
    addClassName(VA_NUMBER_FIELD_CLASSNAME);
  }

  public VANumberField(@Nonnull final String label,
                       @Nonnull final ValueChangeListener<? super ComponentValueChangeEvent<NumberField, Double>> listener) {
    super(label, listener);
    addClassName(VA_NUMBER_FIELD_CLASSNAME);
  }

  public VANumberField(@Nonnull final String label,
                       @Nonnull final Double initialValue,
                       @Nonnull final ValueChangeListener<? super ComponentValueChangeEvent<NumberField, Double>> listener) {
    super(label, initialValue, listener);
    addClassName(VA_NUMBER_FIELD_CLASSNAME);
  }

  private void init(){

  }

}
