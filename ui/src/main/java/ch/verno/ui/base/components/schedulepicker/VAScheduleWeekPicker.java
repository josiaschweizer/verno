package ch.verno.ui.base.components.schedulepicker;

import ch.verno.common.db.dto.YearWeekDto;
import ch.verno.common.util.Publ;
import ch.verno.ui.base.components.notification.NotificationFactory;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@CssImport("./components/va-schedule-week-picker.css")
public class VAScheduleWeekPicker extends CustomField<Set<YearWeekDto>> {

  @Nonnull
  private Integer currentYear;
  @Nonnull
  private final Map<Integer, Set<Integer>> selectedYearWeeksMap;
  @Nullable
  private Integer quantityProposalCourseDays;
  @Nullable
  private ComboBox<Integer> yearSelect;
  @Nullable
  private CheckboxGroup<Integer> weekSelect;
  @Nonnull
  private final Div previewText;

  private boolean internalUpdate = false;
  private boolean enforceQuantitySetting;

  public VAScheduleWeekPicker() {
    this(Publ.EMPTY_STRING);
  }

  public VAScheduleWeekPicker(@Nonnull final String label) {
    this.currentYear = LocalDate.now().getYear();
    this.selectedYearWeeksMap = new HashMap<>();
    this.previewText = new Div();

    setLabel(label);

    initUI();
  }

  private void initUI() {
    addClassName("schedule-week-picker");
    setWidthFull();

    yearSelect = createYearComboBox();
    weekSelect = createWeekSelect();
    final var previewLayout = createPreviewLayout();

    final var content = new VerticalLayout();
    content.addClassName("schedule-week-picker-content");
    content.setPadding(false);
    content.setSpacing(false);
    content.setWidthFull();

    content.add(yearSelect, weekSelect, previewLayout);

    add(content);

    updatePreview();
  }

  @Nonnull
  private ComboBox<Integer> createYearComboBox() {
    final var combobox = new ComboBox<Integer>();
    combobox.setPlaceholder(getTranslation("base.year"));
    combobox.setWidthFull();
    combobox.addClassName("schedule-week-picker-year-combobox");
    combobox.addValueChangeListener(this::selectedYearChanged);

    final var nowYear = LocalDate.now().getYear();
    final List<Integer> yearOptions = IntStream.rangeClosed(nowYear - 1, nowYear + 2)
            .boxed()
            .toList();

    combobox.setItems(yearOptions);
    combobox.setValue(nowYear);

    this.currentYear = nowYear;

    return combobox;
  }

  private void selectedYearChanged(@Nonnull final ValueChangeEvent<Integer> event) {
    if (weekSelect == null) {
      return;
    }

    selectedYearWeeksMap.put(currentYear, new HashSet<>(weekSelect.getSelectedItems()));
    selectedYearWeeksMap.put(currentYear, new HashSet<>(weekSelect.getSelectedItems()));

    final var newYear = event.getValue();
    if (newYear == null) {
      return;
    }

    final var selectedForNewYear = new HashSet<>(selectedYearWeeksMap.getOrDefault(newYear, Set.of()));

    internalUpdate = true;
    try {
      currentYear = newYear;

      weekSelect.setItems(weeksOfYear(newYear));
      weekSelect.setValue(selectedForNewYear);
    } finally {
      internalUpdate = false;
    }

    updatePreview();
  }

  @Nonnull
  private HorizontalLayout createPreviewLayout() {
    final var layout = new HorizontalLayout();
    layout.addClassName("schedule-week-picker-preview");
    layout.setPadding(false);
    layout.setSpacing(false);
    layout.setWidthFull();

    previewText.addClassName("schedule-week-picker-preview-text");
    previewText.setWidthFull();

    layout.add(previewText);
    return layout;
  }

  private CheckboxGroup<Integer> createWeekSelect() {
    final var checkboxGroup = new CheckboxGroup<Integer>();
    checkboxGroup.setLabel(getTranslation("base.week"));
    checkboxGroup.setItems(weeksOfYear(currentYear));
    checkboxGroup.addClassName("schedule-week-picker-week-select");
    checkboxGroup.addValueChangeListener(this::selectedWeeksChanged);
    return checkboxGroup;
  }

  private void selectedWeeksChanged(@Nonnull final ValueChangeEvent<Set<Integer>> event) {
    if (internalUpdate) {
      return;
    }

    final Set<Integer> newValue = new HashSet<>(event.getValue());
    final Set<Integer> oldValue = new HashSet<>(event.getOldValue());

    final var added = new HashSet<>(newValue);
    added.removeAll(oldValue);
    final var isAdding = !added.isEmpty();

    final int totalSelectedBefore = getWeekOverAllYears().size();
    final int limit = (quantityProposalCourseDays != null) ? quantityProposalCourseDays : Integer.MAX_VALUE;

    if (enforceQuantitySetting && quantityProposalCourseDays != null) {
      if (isAdding && totalSelectedBefore >= limit) {
        internalUpdate = true;
        try {
          event.getHasValue().setValue(oldValue);
        } finally {
          internalUpdate = false;
        }

        NotificationFactory.showWarningNotification(
                getTranslation("base.you.have.reached.the.maximum.number.of.allowed.weeks") +
                        Publ.SPACE + Publ.LEFT_PARENTHESIS + limit + Publ.RIGHT_PARENTHESIS + Publ.DOT);
        return;
      }
    }

    setInvalid(false);
    setErrorMessage(null);

    selectedYearWeeksMap.put(currentYear, newValue);
    setModelValue(generateModelValue(), true);
    updatePreview();
  }

  @Nonnull
  @Override
  protected Set<YearWeekDto> generateModelValue() {
    final var allWeeks = new ArrayList<YearWeekDto>();

    selectedYearWeeksMap.forEach((year, weeks) -> {
      for (Integer week : weeks) {
        allWeeks.add(new YearWeekDto(year, week));
      }
    });

    allWeeks.sort(Comparator.comparingInt(YearWeekDto::year)
            .thenComparingInt(YearWeekDto::week));

    return new LinkedHashSet<>(allWeeks);
  }

  @Override
  protected void setPresentationValue(@Nullable final Set<YearWeekDto> value) {
    if (weekSelect == null || yearSelect == null) {
      return;
    }

    internalUpdate = true;
    try {
      selectedYearWeeksMap.clear();

      if (value != null) {
        for (YearWeekDto yw : value) {
          selectedYearWeeksMap
                  .computeIfAbsent(yw.year(), k -> new HashSet<>())
                  .add(yw.week());
        }
      }

      final var selectedYear = yearSelect.getValue();
      currentYear = (selectedYear != null) ? selectedYear : LocalDate.now().getYear();

      weekSelect.setItems(weeksOfYear(currentYear));
      final var selectedForYear = selectedYearWeeksMap.getOrDefault(currentYear, Set.of());
      weekSelect.setValue(new HashSet<>(selectedForYear));
    } finally {
      internalUpdate = false;
    }

    updatePreview();
  }

  private void updatePreview() {
    final var parts = getWeekOverAllYears();

    final var previewTitle = getPreviewTitle(parts);
    final var weeksString = String.join(Publ.COMMA + Publ.SPACE, parts);
    if (parts.isEmpty()) {
      previewText.setText(previewTitle + getTranslation("base.none"));
    } else {
      previewText.setText(previewTitle + weeksString);

      getPreviewTitle(parts);
    }
  }

  @Nonnull
  private String getPreviewTitle(@Nonnull final ArrayList<String> parts) {
    if (quantityProposalCourseDays != null) {
      if (parts.size() > quantityProposalCourseDays) {
        previewText.addClassName("schedule-week-picker-preview-text-red");
      } else {
        previewText.removeClassName("schedule-week-picker-preview-text-red");
        previewText.addClassName("schedule-week-picker-preview-text-normal");
      }
      return getTranslation("base.weeks") + Publ.SPACE + Publ.LEFT_PARENTHESIS + parts.size() +
              Publ.SPACE + Publ.SLASH + Publ.SPACE +
              quantityProposalCourseDays + Publ.RIGHT_PARENTHESIS + Publ.COLON + Publ.SPACE;
    }

    return getTranslation("base.weeks") + Publ.SPACE + Publ.LEFT_PARENTHESIS + parts.size() +
            Publ.RIGHT_PARENTHESIS + Publ.COLON + Publ.SPACE;
  }

  public void setQuantityProposalCourseDays(@Nullable final Integer quantityProposalCourseDays) {
    this.quantityProposalCourseDays = quantityProposalCourseDays;
  }

  public void setEnforceQuantitySetting(final boolean enforceQuantitySetting) {
    this.enforceQuantitySetting = enforceQuantitySetting;
  }

  @Nonnull
  private List<Integer> weeksOfYear(final int year) {
    final int max = maxIsoWeekOfYear(year);
    return IntStream.rangeClosed(1, max).boxed().collect(Collectors.toList());
  }

  private static int maxIsoWeekOfYear(final int year) {
    final var wf = WeekFields.ISO;
    return LocalDate.of(year, 12, 28).get(wf.weekOfWeekBasedYear());
  }

  @Nonnull
  private ArrayList<String> getWeekOverAllYears() {
    final var parts = new ArrayList<String>();
    selectedYearWeeksMap.forEach((year, weeks) -> {
      if (weeks == null) {
        return;
      }

      weeks.stream()
              .sorted()
              .forEach(week -> parts.add("KW" + Publ.MINUS + week + Publ.MINUS + year));
    });
    return parts;
  }
}