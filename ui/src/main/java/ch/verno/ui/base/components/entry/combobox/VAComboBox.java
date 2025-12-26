package ch.verno.ui.base.components.entry.combobox;

import com.vaadin.flow.component.combobox.ComboBox;
import jakarta.annotation.Nonnull;

import java.util.Collection;

public class VAComboBox<T> extends ComboBox<T> {

  public VAComboBox(final int pageSize) {
    super(pageSize);
    addClassName("va-combo-box");
  }

  public VAComboBox() {
    super(50);
    addClassName("va-combo-box");
  }

  public VAComboBox(@Nonnull final String label) {
    super(label);
    addClassName("va-combo-box");
  }


  public VAComboBox(@Nonnull final String label,
                    @Nonnull final Collection<T> items) {
    super(label, items);
    addClassName("va-combo-box");
  }

  @SafeVarargs
  public VAComboBox(@Nonnull final String label,
                    @Nonnull final T... items) {
    super(label, items);
    addClassName("va-combo-box");
  }

  public VAComboBox(@Nonnull final ValueChangeListener<ComponentValueChangeEvent<ComboBox<T>, T>> listener) {
    super(listener);
    addClassName("va-combo-box");
  }

  public VAComboBox(@Nonnull final String label,
                    @Nonnull final ValueChangeListener<ComponentValueChangeEvent<ComboBox<T>, T>> listener) {
    super(label, listener);
    addClassName("va-combo-box");
  }

  @SafeVarargs
  public VAComboBox(@Nonnull final String label,
                    @Nonnull final ValueChangeListener<ComponentValueChangeEvent<ComboBox<T>, T>> listener,
                    @Nonnull final T... items) {
    super(label, listener, items);
    addClassName("va-combo-box");
  }

}
