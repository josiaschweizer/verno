package ch.verno.ui.base.components.schedulepicker;

import ch.verno.common.db.dto.YearWeekDto;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CourseScheduleWeekPicker extends CustomField<Set<YearWeekDto>> {

  private final ComboBox<Integer> yearSelect = new ComboBox<>("Jahr");
  private final CheckboxGroup<Integer> weekSelect = new CheckboxGroup<>();
  private final Div selectionPreview = new Div();

  private final Map<Integer, Set<Integer>> selectedWeeksByYear = new HashMap<>();
  private Integer currentYear;

  public CourseScheduleWeekPicker() {
    final int nowYear = LocalDate.now().getYear();
    final List<Integer> yearOptions = IntStream.rangeClosed(nowYear - 1, nowYear + 2)
            .boxed()
            .collect(Collectors.toList());

    yearSelect.setItems(yearOptions);
    yearSelect.setValue(nowYear);
    currentYear = nowYear;

    weekSelect.setLabel("Kalenderwochen");
    weekSelect.setItems(weeksOfYear(nowYear));
    weekSelect.addValueChangeListener(e -> {
      selectedWeeksByYear.put(currentYear, new HashSet<>(weekSelect.getSelectedItems()));
      updatePreview();
      setModelValue(generateModelValue(), true);
    });

    yearSelect.addValueChangeListener(e -> {
      if (e.getValue() == null) return;

      selectedWeeksByYear.put(currentYear, new HashSet<>(weekSelect.getSelectedItems()));

      currentYear = e.getValue();
      weekSelect.setItems(weeksOfYear(currentYear));

      final Set<Integer> selectedForYear = selectedWeeksByYear.getOrDefault(currentYear, Set.of());
      weekSelect.deselectAll();
      selectedForYear.forEach(weekSelect::select);

      updatePreview();
      setModelValue(generateModelValue(), true);
    });

    final var fromYear = new ComboBox<Integer>("Von Jahr");
    fromYear.setItems(yearOptions);
    final var toYear = new ComboBox<Integer>("Bis Jahr");
    toYear.setItems(yearOptions);
    fromYear.setValue(nowYear);
    toYear.setValue(nowYear);

    final var fromWeek = new IntegerField("Von KW");
    fromWeek.setMin(1);
    fromWeek.setMax(53);
    final var toWeek = new IntegerField("Bis KW");
    toWeek.setMin(1);
    toWeek.setMax(53);

    final var root = new VerticalLayout(yearSelect, weekSelect, selectionPreview);
    root.setPadding(false);
    root.setSpacing(true);

    add(root);
    updatePreview();
  }

  @Nonnull
  @Override
  protected Set<YearWeekDto> generateModelValue() {
    final var allWeeks = new ArrayList<YearWeekDto>();

    selectedWeeksByYear.forEach((year, weeks) -> {
      for (Integer week : weeks) {
        allWeeks.add(new YearWeekDto(year, week));
      }
    });
    allWeeks.sort(Comparator.comparingInt(YearWeekDto::year).thenComparingInt(YearWeekDto::week));
    return new LinkedHashSet<>(allWeeks);
  }

  @Override
  protected void setPresentationValue(@Nullable final Set<YearWeekDto> value) {
    selectedWeeksByYear.clear();

    if (value != null) {
      for (YearWeekDto yw : value) {
        selectedWeeksByYear.computeIfAbsent(yw.year(), k -> new HashSet<>()).add(yw.week());
      }
    }

    currentYear = yearSelect.getValue();

    weekSelect.setItems(weeksOfYear(currentYear));
    final var selectedForYear = selectedWeeksByYear.getOrDefault(currentYear, Set.of());
    weekSelect.deselectAll();
    selectedForYear.forEach(weekSelect::select);

    updatePreview();
  }

  private void updatePreview() {
    selectionPreview.removeAll();
    final var sorted = generateModelValue();
    if (sorted.isEmpty()) {
      selectionPreview.add(new Span("Keine KW ausgewählt."));
      return;
    }

    final String text = sorted.stream()
            .map(YearWeekDto::toString)
            .collect(Collectors.joining(", "));

    selectionPreview.add(new Span("Auswahl: " + text));
  }

  @Nonnull
  private static List<Integer> weeksOfYear(final int year) {
    final int max = maxIsoWeekOfYear(year);
    return IntStream.rangeClosed(1, max).boxed().collect(Collectors.toList());
  }

  private static int maxIsoWeekOfYear(final int year) {
    final var wf = WeekFields.ISO;
    return LocalDate.of(year, 12, 28).get(wf.weekOfWeekBasedYear());
  }
}