package ch.verno.ui.base.components.entry.email;

import com.vaadin.flow.component.textfield.EmailField;
import jakarta.annotation.Nonnull;

public class VAEmailField extends EmailField {

  public VAEmailField() {
    super();
  }

  public VAEmailField(@Nonnull final String label) {
    super(label);
  }

  public VAEmailField(@Nonnull final String label, @Nonnull final String placeholder) {
    super(label, placeholder);
  }

  public VAEmailField(@Nonnull final ValueChangeListener<? super ComponentValueChangeEvent<EmailField, String>> listener) {
    super(listener);
  }

  public VAEmailField(@Nonnull final String label,
                      @Nonnull final ValueChangeListener<? super ComponentValueChangeEvent<EmailField, String>> listener) {
    super(label, listener);
  }

  public VAEmailField(@Nonnull final String label,
                      @Nonnull final String initialValue,
                      @Nonnull final ValueChangeListener<? super ComponentValueChangeEvent<EmailField, String>> listener) {
    super(label, initialValue, listener);
  }
}
