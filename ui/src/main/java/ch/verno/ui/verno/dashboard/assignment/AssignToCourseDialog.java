package ch.verno.ui.verno.dashboard.assignment;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.util.Publ;
import ch.verno.server.service.CourseService;
import ch.verno.server.service.MandantSettingService;
import ch.verno.server.service.ParticipantService;
import ch.verno.ui.base.components.entry.combobox.VAComboBox;
import ch.verno.ui.base.components.filter.VASearchFilter;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.factory.EntryFactory;
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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
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
  private final Map<Long, String> participantItems;

  private boolean suppressSelectionSync;

  public AssignToCourseDialog(@Nonnull final CourseService courseService,
                              @Nonnull final ParticipantService participantService,
                              @Nonnull final MandantSettingService mandantSettingService) {
    this.courseService = courseService;
    this.participantService = participantService;
    this.mandantSettingService = mandantSettingService;

    this.selectedParticipantIds = new LinkedHashSet<>();
    this.participantItems = new LinkedHashMap<>();

    setHeight("60vh");

    saveButton = createSaveButton();
    final var cancelButton = createCancelButton();

    setHeaderTitle("Assign Participants to Course");
    add(createContent());
    getFooter().add(cancelButton);
    getFooter().add(saveButton);
  }

  @Nonnull
  private HorizontalLayout createContent() {
    final var left = createCourseLayout();
    final var right = createParticipantLayout();

    final var layout = new HorizontalLayout(left, right);
    layout.setWidthFull();
    layout.setHeightFull();
    layout.setAlignItems(FlexComponent.Alignment.STRETCH);
    layout.addClassNames(LumoUtility.Gap.XLARGE);

    right.setHeightFull();
    layout.setFlexGrow(1, left, right);

    return layout;
  }

  @Nonnull
  private VerticalLayout createCourseLayout() {
    final var title = createTitleSpan("Course");
    courseComboBox = createCourseComboBox();

    return createLayoutFromComponents(title, courseComboBox);
  }

  @Nonnull
  private VerticalLayout createParticipantLayout() {
    final var title = createTitleSpan("Participants");
    final var searchBar = new VASearchFilter("Search participants…");
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

    final var searchString = searchTerm == null ?
            Publ.EMPTY_STRING :
            searchTerm.toLowerCase(Locale.ROOT);

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
                participant.getCourseLevel().getId() != null &&
                courseLevel.getId().equals(participant.getCourseLevel().getId())) {
          return true;
        }
      }
      return false;
    }
    return true;
  }

  @Nonnull
  private Button createSaveButton() {
    final var saveButton = new Button("Save");
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
    selectedParticipantIds.forEach(participantId -> {
      final var participant = participantService.getParticipantById(participantId);
      participant.setCourse(course);
      participantService.updateParticipant(participant);
    });

    NotificationFactory.showSuccessNotification("Assigned " + selectedParticipantIds.size()
            + " participants to course '"
            + course.getTitle()
            + Publ.SIMPLE_QUOTE
            + Publ.DOT);
    close();
  }

  @Nonnull
  private Button createCancelButton() {
    final var button = new Button("Cancel");
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

    this.courseComboBox.addValueChangeListener(e -> {
      applyParticipantFilterToUi();
      updateSaveEnabled();
    });
    return this.courseComboBox;
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
    participantsGroup.setItemLabelGenerator(id -> participantItems.getOrDefault(id, "Participant #" + id));
    participantsGroup.addClassName("participants-group");
    participantsGroup.setWidthFull();

    participantsGroup.addValueChangeListener(e -> {
      if (suppressSelectionSync) {
        return;
      }
      selectedParticipantIds = new LinkedHashSet<>(e.getValue());
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
