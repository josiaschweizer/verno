package ch.verno.ui.base.settings.fields;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

public class VABaseComboBoxSetting<T> extends VABaseSettingRow {

  @NonNull
  private final ComboBox<T> comboBox;

  public VABaseComboBoxSetting(@NonNull final String title) {
    this(title, new ComboBox<>());
  }

  public VABaseComboBoxSetting(@NonNull final String title,
                               @NonNull final ComboBox<T> comboBox) {
    super(title, comboBox);
    this.comboBox = comboBox;

    this.comboBox.setWidthFull();
  }

  @NonNull
  public ComboBox<T> getComboBox() {
    return comboBox;
  }

  public void setItems(@NonNull final Collection<T> items) {
    comboBox.setItems(new ListDataProvider<>(items));
  }

  public void setItemLabelGenerator(@NonNull final Function<T, String> labelGenerator) {
    comboBox.setItemLabelGenerator(item ->
            Objects.requireNonNullElse(labelGenerator.apply(item), "")
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