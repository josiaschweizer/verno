package ch.verno.ui.verno.dashboard.assignment;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.util.Publ;
import ch.verno.server.service.CourseService;
import ch.verno.server.service.MandantSettingService;
import ch.verno.server.service.ParticipantService;
import ch.verno.ui.base.components.entry.combobox.VAComboBox;
import ch.verno.ui.base.components.filter.VASearchFilter;
import ch.verno.ui.base.components.notification.NotificationFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.*;
import java.util.stream.Collectors;

@CssImport("/components/assignment/assignment.css")
public class AssignToCourseDialog extends Dialog {

  @Nonnull
  private final CourseService courseService;
  @Nonnull
  private final ParticipantService participantService;
  @Nonnull
  private final MandantSettingService mandantSettingService;

  @Nullable
  private final Long preSelectedCourseId;

  @Nullable
  private CheckboxGroup<Long> participantsGroup;
  @Nullable
  private VAComboBox<Long> courseComboBox;
  @Nullable
  private final Button saveButton;
  @Nullable
  private String searchTerm;
  @Nonnull
  private LinkedHashSet<Long> selectedParticipantIds;
  @Nonnull
  private final LinkedHashSet<Long> unselectedParticipantIds;
  @Nonnull
  private final Map<Long, String> participantItems;

  private boolean suppressSelectionSync;

  public AssignToCourseDialog(@Nonnull final CourseService courseService,
                              @Nonnull final ParticipantService participantService,
                              @Nonnull final MandantSettingService mandantSettingService) {
    this(courseService, participantService, mandantSettingService, null, List.of());
  }

  public AssignToCourseDialog(@Nonnull final CourseService courseService,
                              @Nonnull final ParticipantService participantService,
                              @Nonnull final MandantSettingService mandantSettingService,
                              @Nullable final Long preSelectedCourseId,
                              @Nonnull final List<Long> preSelectedParticipantIds) {
    this.courseService = courseService;
    this.participantService = participantService;
    this.mandantSettingService = mandantSettingService;
    this.preSelectedCourseId = preSelectedCourseId;

    this.selectedParticipantIds = new LinkedHashSet<>(preSelectedParticipantIds);
    this.unselectedParticipantIds = new LinkedHashSet<>();
    this.participantItems = new LinkedHashMap<>();

    setHeight("80vh");
    setWidth("min(1500px, 95vw)");
    setMaxWidth("1500px");
    setMinWidth("320px");

    saveButton = createSaveButton();
    final var cancelButton = createCancelButton();

    setHeaderTitle(getTranslation("participant.assign.participants.to.course"));
    add(createContent());
    getFooter().add(cancelButton);
    getFooter().add(saveButton);
  }

  @Nonnull
  private HorizontalLayout createContent() {
    final var left = createCourseLayout();
    left.getElement().getStyle().set("min-width", "260px");
    left.getElement().getStyle().set("flex", "1 1 260px");
    final var right = createParticipantLayout();
    right.getElement().getStyle().set("min-width", "260px");
    right.getElement().getStyle().set("flex", "1 1 260px");

    final var layout = new HorizontalLayout(left, right);
    layout.setWidthFull();
    layout.setHeightFull();
    layout.setAlignItems(FlexComponent.Alignment.STRETCH);
    layout.addClassNames(LumoUtility.Gap.XLARGE);
    layout.getStyle().set("flex-wrap", "wrap");

    right.setHeightFull();
    layout.setFlexGrow(1, left, right);

    return layout;
  }

  @Nonnull
  private VerticalLayout createCourseLayout() {
    final var title = createTitleSpan(getTranslation("course.course"));
    courseComboBox = createCourseComboBox();

    return createLayoutFromComponents(title, courseComboBox);
  }

  @Nonnull
  private VerticalLayout createParticipantLayout() {
    final var title = createTitleSpan(getTranslation("participant.participants"));
    final var searchBar = new VASearchFilter(getTranslation("participant.search.participants"));
    searchBar.addValueChangeListener(e -> searchChanged(e.getValue()));
    final var participants = createParticipantsCheckboxGroup();

    final var layout = createLayoutFromComponents(title, searchBar, participants);
    layout.setFlexGrow(1, participants);
    return layout;
  }

  @Nonnull
  private Span createTitleSpan(@Nonnull final String titleLabel) {
    final var title = new Span(titleLabel);
    title.addClassNames("va-required");
    title.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.SEMIBOLD);
    return title;
  }

  private void searchChanged(@Nullable final String term) {
    this.searchTerm = term == null ? null : term.trim();
    applyParticipantFilterToUi();
  }

  private void applyParticipantFilterToUi() {
    if (participantsGroup == null) {
      return;
    }

    final var searchString = searchTerm == null ? Publ.EMPTY_STRING : searchTerm.toLowerCase(Locale.ROOT);

    final var filtered = participantItems.entrySet().stream()
            .filter(e -> searchString.isEmpty() || e.getValue().toLowerCase(Locale.ROOT).contains(searchString))
            .map(Map.Entry::getKey)
            .collect(Collectors.toCollection(LinkedHashSet::new))
            .stream()
            .filter(this::filterForInvalidCourseLevel)
            .collect(Collectors.toCollection(LinkedHashSet::new));

    selectedParticipantIds = selectedParticipantIds.stream()
            .filter(this::filterForInvalidCourseLevel)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    filtered.addAll(selectedParticipantIds);

    suppressSelectionSync = true;
    try {
      participantsGroup.setItems(filtered);
      participantsGroup.setValue(selectedParticipantIds);
    } finally {
      suppressSelectionSync = false;
    }
  }

  private boolean filterForInvalidCourseLevel(@Nonnull final Long id) {
    if (mandantSettingService.getSingleMandantSetting().isEnforceCourseLevelSettings() &&
            courseComboBox != null &&
            courseComboBox.getValue() != null) {
      final var course = courseService.getCourseById(courseComboBox.getValue());
      final var participant = participantService.getParticipantById(id);

      for (final var courseLevel : course.getCourseLevels()) {
        if (courseLevel.getId() != null &&
                !participant.getCourseLevels().isEmpty() &&
                participant.getCourseLevels().stream().map(CourseLevelDto::getId).toList().contains(courseLevel.getId())) {
          return true;
        }
      }
      return false;
    }
    return true;
  }

  @Nonnull
  private Button createSaveButton() {
    final var saveButton = new Button(getTranslation("common.save"));
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.setEnabled(false);
    saveButton.addClickListener(event -> save());
    return saveButton;
  }

  private void updateSaveEnabled() {
    if (saveButton == null || courseComboBox == null || participantsGroup == null) {
      return;
    }

    final var courseOk = courseComboBox.getValue() != null;
    final var participantsOk = !participantsGroup.getValue().isEmpty();

    saveButton.setEnabled(courseOk && participantsOk);
  }

  private void save() {
    if (courseComboBox == null) {
      return;
    }

    final var course = courseService.getCourseById(courseComboBox.getValue());
    selectedParticipantIds.forEach(id -> {
      final var participant = participantService.getParticipantById(id);
      participant.addCourse(course);
      participantService.updateParticipant(participant);
    });
    unselectedParticipantIds.forEach(id -> {
      final var participant = participantService.getParticipantById(id);
      participant.getCourses().removeIf(c -> Objects.equals(c.getId(), course.getId()));
      participantService.updateParticipant(participant);
    });

    NotificationFactory.showSuccessNotification(getTranslation("shared.assigned") + Publ.SPACE + selectedParticipantIds.size()
            + Publ.SPACE
            + getTranslation("shared.participants.to.course")
            + Publ.SPACE
            + Publ.SIMPLE_QUOTE
            + course.getTitle()
            + Publ.SIMPLE_QUOTE
            + Publ.DOT);
    close();
  }

  @Nonnull
  private Button createCancelButton() {
    final var button = new Button(getTranslation("shared.cancel"));
    button.addClickListener(event -> close());
    return button;
  }

  @Nonnull
  private VerticalLayout createLayoutFromComponents(@Nonnull final Component... components) {
    final var layout = new VerticalLayout(components);
    layout.setPadding(false);
    layout.setSpacing(false);
    return layout;
  }

  @Nonnull
  private VAComboBox<Long> createCourseComboBox() {
    final var options = courseService.getAllCourses().stream()
            .collect(Collectors.toMap(CourseDto::getId, CourseDto::getTitle));

    courseComboBox = new VAComboBox<>();
    courseComboBox.setWidthFull();

    courseComboBox.setItems(options.keySet());
    courseComboBox.setItemLabelGenerator(id -> options.getOrDefault(id, String.valueOf(id)));
    courseComboBox.setClearButtonVisible(true);
    courseComboBox.setValue(preSelectedCourseId);

    courseComboBox.addValueChangeListener(e -> {
      applyParticipantFilterToUi();
      updateSaveEnabled();
    });
    return courseComboBox;
  }

  @Nonnull
  private Scroller createParticipantsCheckboxGroup() {
    participantItems.clear();
    participantService.getAllParticipants().stream()
            .filter(ParticipantDto::isActive)
            .forEach(p ->
                    participantItems.put(p.getId(), p.getFullName())
            );

    participantsGroup = new CheckboxGroup<>();
    participantsGroup.setItemLabelGenerator(id -> participantItems.getOrDefault(id, getTranslation("participant.participant") + Publ.SPACE + Publ.HASH + id));
    participantsGroup.addClassName("participants-group");
    participantsGroup.setWidthFull();

    participantsGroup.addValueChangeListener(e -> {
      if (suppressSelectionSync) {
        return;
      }

      final var oldSelected = new LinkedHashSet<>(selectedParticipantIds);
      final var newSelected = new LinkedHashSet<>(e.getValue());

      final var deselected = oldSelected.stream()
              .filter(id -> !newSelected.contains(id))
              .toList();

      final var selectedAgain = newSelected.stream()
              .filter(id -> !oldSelected.contains(id))
              .toList();

      selectedParticipantIds = newSelected;
      unselectedParticipantIds.addAll(deselected);
      selectedAgain.forEach(unselectedParticipantIds::remove);

      updateSaveEnabled();
    });

    applyParticipantFilterToUi();

    final var scroller = new Scroller();
    scroller.setWidthFull();
    scroller.setHeightFull();
    scroller.setContent(participantsGroup);
    return scroller;
  }
}
