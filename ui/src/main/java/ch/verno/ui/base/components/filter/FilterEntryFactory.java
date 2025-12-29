package ch.verno.ui.base.components.filter;

import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;

import java.util.Map;
import java.util.Set;

public class FilterEntryFactory<T> {

  @Nonnull
  public MultiSelectComboBox<Long> createComboboxFilter(@Nonnull final ValueProvider<T, Set<Long>> valueProvider,
                                                        @Nonnull final Setter<T, Set<Long>> valueSetter,
                                                        @Nonnull final Map<Long, String> options,
                                                        @Nonnull final Binder<T> binder,
                                                        @Nonnull final String label) {
    final var comboBox = new MultiSelectComboBox<Long>();
    comboBox.setLabel(label);
    comboBox.setItems(options.keySet());
    comboBox.setItemLabelGenerator(options::get);
    comboBox.setWidthFull();
    binder.forField(comboBox)
            .bind(valueProvider, valueSetter);
    return comboBox;
  }

}
