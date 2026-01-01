package ch.verno.ui.base.components.entry.weekoption;

import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.customfield.CustomField;
import jakarta.annotation.Nonnull;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.*;

public class VAWeekOption extends CustomField<List<DayOfWeek>> {

  private final CheckboxGroup<DayOfWeek> group = new CheckboxGroup<>();

  public VAWeekOption() {
    this(null);
  }

  public VAWeekOption(final String label) {
    addClassName("va-week-option");

    if (label != null) {
      setLabel(label);
    }

    group.setItems(DayOfWeek.values());
    group.setItemLabelGenerator(d -> d.getDisplayName(TextStyle.FULL_STANDALONE, Locale.GERMAN));
    add(group);

    group.addValueChangeListener(e -> updateValue());
  }

  @Nonnull
  public CheckboxGroup<DayOfWeek> getCheckboxGroup() {
    return group;
  }

  @Override
  protected List<DayOfWeek> generateModelValue() {
    final Set<DayOfWeek> selected = group.getValue() == null ? EnumSet.noneOf(DayOfWeek.class) : group.getValue();

    final var ordered = new ArrayList<DayOfWeek>(7);
    for (final var d : DayOfWeek.values()) {
      if (selected.contains(d)) {
        ordered.add(d);
      }
    }

    return ordered;
  }

  @Override
  protected void setPresentationValue(final List<DayOfWeek> value) {
    if (value == null || value.isEmpty()) {
      group.setValue(EnumSet.noneOf(DayOfWeek.class));
      return;
    }

    final var filteredValue = value.stream().filter(Objects::nonNull).toList();
    if (filteredValue.isEmpty()) {
      group.setValue(EnumSet.noneOf(DayOfWeek.class));
      return;
    }

    group.setValue(EnumSet.copyOf(filteredValue));
  }
}