package ch.verno.ui.base.components.filter;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FilterEntryFactory<T> {

  @Nonnull
  public <ID> MultiSelectComboBox<ID> createMultiSelectComboboxFilter(@Nonnull final ValueProvider<T, Set<ID>> valueProvider,
                                                                      @Nonnull final Setter<T, Set<ID>> valueSetter,
                                                                      @Nonnull final Map<ID, String> options,
                                                                      @Nonnull final Binder<T> binder,
                                                                      @Nonnull final String label) {
    final var comboBox = new MultiSelectComboBox<ID>();
    comboBox.setLabel(label);
    comboBox.setItems(options.keySet());
    comboBox.setItemLabelGenerator(options::get);
    comboBox.setWidthFull();
    binder.forField(comboBox).bind(valueProvider, valueSetter);
    return comboBox;
  }

  @Nonnull
  public <ID> MultiSelectComboBox<ID> createMultiSelectComboboxFilter(@Nonnull final ValueProvider<T, Set<ID>> valueProvider,
                                                                      @Nonnull final Setter<T, Set<ID>> valueSetter,
                                                                      @Nonnull final List<ID> items,
                                                                      @Nonnull final ItemLabelGenerator<ID> labelGenerator,
                                                                      @Nonnull final Binder<T> binder,
                                                                      @Nonnull final String label) {
    final var comboBox = new MultiSelectComboBox<ID>();
    comboBox.setLabel(label);
    comboBox.setItems(items);
    comboBox.setItemLabelGenerator(labelGenerator);
    comboBox.setWidthFull();
    binder.forField(comboBox).bind(valueProvider, valueSetter);
    return comboBox;
  }

  @Nonnull
  public <ID> ComboBox<ID> createComboBoxFilter(@Nonnull final ValueProvider<T, ID> valueProvider,
                                                @Nonnull final Setter<T, ID> valueSetter,
                                                @Nonnull final Map<ID, String> options,
                                                @Nonnull final Binder<T> binder,
                                                @Nonnull final String label) {
    final var comboBox = new ComboBox<ID>();
    comboBox.setLabel(label);
    comboBox.setItems(options.keySet());
    comboBox.setItemLabelGenerator(options::get);
    comboBox.setWidthFull();
    binder.forField(comboBox)
            .bind(valueProvider, valueSetter);
    return comboBox;
  }

  @Nonnull
  public <ID> CheckboxGroup<ID> createCheckboxGroupEntry(@Nonnull final ValueProvider<T, Set<ID>> valueProvider,
                                                         @Nonnull final Setter<T, Set<ID>> valueSetter,
                                                         @Nonnull final Binder<T> binder,
                                                         @Nonnull final Optional<String> required,
                                                         @Nonnull final String label,
                                                         @Nonnull final Map<ID, String> options) {
    final var checkboxGroup = new CheckboxGroup<ID>();
    checkboxGroup.setLabel(label);
    checkboxGroup.setWidthFull();

    checkboxGroup.setItems(options.keySet());
    checkboxGroup.setItemLabelGenerator(options::get);

    bindEntry(checkboxGroup, valueProvider, valueSetter, binder, required);

    return checkboxGroup;
  }

  private <FIELDVALUE> void bindEntry(@Nonnull final HasValue<?, FIELDVALUE> entry,
                                      @Nonnull final ValueProvider<T, FIELDVALUE> valueProvider,
                                      @Nonnull final Setter<T, FIELDVALUE> valueSetter,
                                      @Nonnull final Binder<T> binder,
                                      @Nonnull final Optional<String> required) {
    final var binding = binder.forField(entry);
    required.ifPresent(binding::asRequired);

    // has to be the last call in the chain
    binding.bind(valueProvider, valueSetter);
  }

}