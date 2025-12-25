package ch.verno.ui.base.components.schedulepicker;

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

public class CourseScheduleWeekPicker extends CustomField<Set<YearWeek>> {

  private final ComboBox<Integer> yearSelect = new ComboBox<>("Jahr");
  private final CheckboxGroup<Integer> weekSelect = new CheckboxGroup<>();
  private final Div selectionPreview = new Div();

  private final ComboBox<Integer> fromYear = new ComboBox<>("Von Jahr");
  private final IntegerField fromWeek = new IntegerField("Von KW");
  private final ComboBox<Integer> toYear = new ComboBox<>("Bis Jahr");
  private final IntegerField toWeek = new IntegerField("Bis KW");

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

    fromYear.setItems(yearOptions);
    toYear.setItems(yearOptions);
    fromYear.setValue(nowYear);
    toYear.setValue(nowYear);

    fromWeek.setMin(1);
    fromWeek.setMax(53);
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
  protected Set<YearWeek> generateModelValue() {
    final var allWeeks = new ArrayList<YearWeek>();

    selectedWeeksByYear.forEach((year, weeks) -> {
      for (Integer week : weeks) {
        allWeeks.add(new YearWeek(year, week));
      }
    });
    allWeeks.sort(Comparator.comparingInt(YearWeek::year).thenComparingInt(YearWeek::week));
    return new LinkedHashSet<>(allWeeks);
  }

  @Override
  protected void setPresentationValue(@Nullable final Set<YearWeek> value) {
    selectedWeeksByYear.clear();

    if (value != null) {
      for (YearWeek yw : value) {
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
            .map(YearWeek::toString)
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