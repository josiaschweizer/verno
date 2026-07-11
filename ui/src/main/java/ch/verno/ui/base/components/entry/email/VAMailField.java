package ch.verno.ui.base.components.entry.email;

import com.vaadin.flow.component.textfield.EmailField;
import jakarta.annotation.Nonnull;

public class VAMailField extends EmailField {

  public VAMailField() {
    super();
  }

  public VAMailField(@Nonnull final String label) {
    super(label);
  }

  public VAMailField(@Nonnull final String label, @Nonnull final String placeholder) {
    super(label, placeholder);
  }

  public VAMailField(@Nonnull final ValueChangeListener<? super ComponentValueChangeEvent<EmailField, String>> listener) {
    super(listener);
  }

  public VAMailField(@Nonnull final String label,
                     @Nonnull final ValueChangeListener<? super ComponentValueChangeEvent<EmailField, String>> listener) {
    super(label, listener);
  }

  public VAMailField(@Nonnull final String label,
                     @Nonnull final String initialValue,
                     @Nonnull final ValueChangeListener<? super ComponentValueChangeEvent<EmailField, String>> listener) {
    super(label, initialValue, listener);
  }
}
