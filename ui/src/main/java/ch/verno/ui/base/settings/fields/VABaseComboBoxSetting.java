package ch.verno.ui.base.settings.fields;

import ch.verno.common.util.Publ;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.ListDataProvider;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

public class VABaseComboBoxSetting<T> extends VABaseSettingRow {

  @Nonnull
  private final ComboBox<T> comboBox;

  public VABaseComboBoxSetting(@Nonnull final String title) {
    this(title, new ComboBox<>());
  }

  public VABaseComboBoxSetting(@Nonnull final String title,
                               @Nonnull final ComboBox<T> comboBox) {
    super(title, comboBox);
    this.comboBox = comboBox;

    this.comboBox.setWidthFull();
  }

  @Nonnull
  public ComboBox<T> getComboBox() {
    return comboBox;
  }

  public void setItems(@Nonnull final Collection<T> items) {
    comboBox.setItems(new ListDataProvider<>(items));
  }

  public void setItemLabelGenerator(@Nonnull final Function<T, String> labelGenerator) {
    comboBox.setItemLabelGenerator(item ->
            Objects.requireNonNullElse(labelGenerator.apply(item), Publ.EMPTY_STRING)
    );
  }

  public T getValue() {
    return comboBox.getValue();
  }

  public void setValue(final T value) {
    comboBox.setValue(value);
  }

  public void setClearButtonVisible(final boolean visible) {
    comboBox.setClearButtonVisible(visible);
  }

  public void setReadOnly(final boolean readOnly) {
    comboBox.setReadOnly(readOnly);
  }
}