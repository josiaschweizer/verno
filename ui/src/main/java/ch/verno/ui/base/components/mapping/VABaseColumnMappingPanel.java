package ch.verno.ui.base.components.mapping;

import ch.verno.lib.Publ;
import ch.verno.lib.VernoUtility;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.ListDataProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;

import java.util.*;
import java.util.stream.Collectors;

public abstract class VABaseColumnMappingPanel<TField> extends Composite<Div> {

  @NonNls public static final String IGNORE_KEY = "__IGNORE__";

  private final boolean allowDuplicates;
  @Nonnull private final String ignoreLabel;
  @Nonnull private final List<TField> availableFields;
  @Nonnull private final Map<String, String> selectionByCsvColumn;

  @Nonnull private final Grid<MappingRow> grid;

  protected VABaseColumnMappingPanel(@Nonnull final List<String> csvColumns,
                                     @Nonnull final List<TField> availableFields,
                                     @Nonnull final String ignoreLabelKey,
                                     final boolean allowDuplicates) {
    this.allowDuplicates = allowDuplicates;
    this.ignoreLabel = getTranslation(ignoreLabelKey);
    this.availableFields = List.copyOf(availableFields);
    this.selectionByCsvColumn = new LinkedHashMap<>();

    final var rows = csvColumns.stream()
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(s -> !s.isBlank())
            .distinct()
            .map(col -> new MappingRow(col, null))
            .toList();
    rows.forEach(r -> selectionByCsvColumn.put(r.csvColumn(), null));

    final var dataProvider = new ListDataProvider<>(new ArrayList<>(rows));

    this.grid = new Grid<>(MappingRow.class, false);
    buildGrid(availableFields);
    grid.setItems(dataProvider);

    getContent().setSizeFull();
    getContent().add(grid);
  }

  private void buildGrid(@Nonnull final List<TField> availableFields) {
    grid.setSizeFull();

    grid.addColumn(MappingRow::csvColumn)
            .setHeader(getCsvColumnHeader())
            .setAutoWidth(true)
            .setFlexGrow(1);

    grid.addComponentColumn(row -> buildFieldCombo(row, availableFields))
            .setHeader(getTargetHeader())
            .setAutoWidth(true)
            .setFlexGrow(1);

    grid.getStyle().setMinHeight(VernoUtility.NONE);
  }

  private ComboBox<FieldOption<TField>> buildFieldCombo(@Nonnull final MappingRow row,
                                                        @Nonnull final List<TField> availableFields) {

    final var comboBox = new ComboBox<FieldOption<TField>>();
    comboBox.setWidthFull();
    comboBox.setClearButtonVisible(true);

    final var options = new ArrayList<FieldOption<TField>>();
    options.add(FieldOption.ignore(ignoreLabel));

    for (final var field : availableFields) {
      options.add(FieldOption.of(getFieldKey(field), buildFieldOptionLabel(field), field));
    }

    comboBox.setItems(options);
    comboBox.setItemLabelGenerator(FieldOption::label);

    final var selectedKey = selectionByCsvColumn.get(row.csvColumn());
    if (selectedKey != null) {
      comboBox.setValue(options.stream().filter(o -> selectedKey.equals(o.key())).findFirst().orElse(null));
    }

    comboBox.addValueChangeListener(e -> {
      final var oldKey = selectionByCsvColumn.get(row.csvColumn());
      final FieldOption<TField> newValue = e.getValue();
      final var newKey = (newValue == null ? null : newValue.key());

      if (!allowDuplicates && newKey != null && !IGNORE_KEY.equals(newKey)) {
        final boolean alreadyUsedByOther = selectionByCsvColumn.entrySet().stream()
                .anyMatch(en -> !en.getKey().equals(row.csvColumn()) && newKey.equals(en.getValue()));

        if (alreadyUsedByOther) {
          final var oldOpt = options.stream().filter(o -> Objects.equals(oldKey, o.key())).findFirst().orElse(null);
          comboBox.setValue(oldOpt);
          return;
        }
      }

      selectionByCsvColumn.put(row.csvColumn(), newKey);
      onMappingChanged(row.csvColumn(), oldKey, newKey);
    });

    return comboBox;
  }

  @Nonnull
  private String buildFieldOptionLabel(@Nonnull final TField field) {
    final var label = getTranslation(getFieldLabel(field));
    final var required = isFieldRequired(field);
    final var alreadyFilled = isFieldAlreadyFilled(field);

    if (required && alreadyFilled) {
      return label
              + Publ.SPACE
              + Publ.REQUIRED_STAR
              + Publ.SPACE
              + Publ.LEFT_PARENTHESIS
              + getTranslation("base.already.filled")
              + Publ.RIGHT_PARENTHESIS;
    } else if (required) {
      return label
              + Publ.SPACE
              + Publ.REQUIRED_STAR;
    } else if (alreadyFilled) {
      return label
              + Publ.SPACE
              + Publ.LEFT_PARENTHESIS
              + getTranslation("base.already.filled")
              + Publ.RIGHT_PARENTHESIS;
    }

    return label;
  }

  @Nonnull
  public Map<String, String> getMapping() {
    return selectionByCsvColumn.entrySet().stream()
            .filter(e -> e.getValue() != null)
            .filter(e -> !IGNORE_KEY.equals(e.getValue()))
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (a, b) -> a,
                    LinkedHashMap::new
            ));
  }

  public boolean isValid() {
    final var mapped = selectionByCsvColumn.values().stream()
            .filter(Objects::nonNull)
            .filter(v -> !IGNORE_KEY.equals(v))
            .toList();

    if (mapped.isEmpty()) {
      return false;
    } else if (!allRequiredFieldsFilled()) {
      return false;
    } else if (allowDuplicates) {
      return true;
    }

    final var set = new HashSet<>(mapped);
    return set.size() == mapped.size();
  }

  private boolean allRequiredFieldsFilled() {
    final var mappedFieldKeys = selectionByCsvColumn.values().stream()
            .filter(Objects::nonNull)
            .filter(v -> !IGNORE_KEY.equals(v))
            .collect(Collectors.toSet());

    return availableFields.stream()
            .filter(this::isFieldRequired)
            .map(this::getFieldKey)
            .allMatch(mappedFieldKeys::contains);
  }

  public void setEnabled(boolean enabled) {
    grid.setEnabled(enabled);
  }

  @Nonnull
  protected String getCsvColumnHeader() {
    return getTranslation("shared.csv.spalte");
  }

  @Nonnull
  protected String getTargetHeader() {
    return getTranslation("shared.map.to.db.field");
  }

  @Nonnull
  protected abstract String getFieldKey(@Nonnull TField field);

  @Nonnull
  protected abstract String getFieldLabel(@Nonnull TField field);

  protected abstract boolean isFieldRequired(@Nonnull TField field);

  protected boolean isFieldAlreadyFilled(@Nonnull final TField field) {
    return false;
  }

  protected void onMappingChanged(@Nonnull final String csvColumn,
                                  @Nullable final String oldFieldKey,
                                  @Nullable final String newFieldKey) {
    // can be overridden by subclasses
  }
}