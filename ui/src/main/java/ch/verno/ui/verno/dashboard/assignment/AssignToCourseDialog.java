package ch.verno.ui.verno.dashboard.assignment;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.db.filter.ParticipantFilter;
import ch.verno.common.util.Publ;
import ch.verno.server.service.CourseService;
import ch.verno.server.service.MandantSettingService;
import ch.verno.server.service.ParticipantService;
import ch.verno.ui.base.components.entry.combobox.VAComboBox;
import ch.verno.ui.base.components.filter.FilterEntryFactory;
import ch.verno.ui.base.components.filter.VAFilterBar;
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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@CssImport("/components/assignment/assignment.css")
public class AssignToCourseDialog extends Dialog {

  @Nonnull
  private final Binder<AssignParticipantsToCourseDto> binder;
  @Nonnull
  private final EntryFactory<AssignParticipantsToCourseDto> entryFactory;
  @Nonnull
  private final FilterEntryFactory<AssignParticipantsToCourseDto, ParticipantFilter> filterFactory;
  @Nonnull
  private final CourseService courseService;
  @Nonnull
  private final ParticipantService participantService;
  @Nonnull
  private final Map<Long, String> participantLabelCache;
  @Nullable
  private final Button saveButton;
  @Nonnull
  private ParticipantFilter participantFilter;
  @Nonnull
  private final ConfigurableFilterDataProvider<Long, Void, ParticipantFilter> participantDataProvider;
  @Nullable
  private CheckboxGroup<Long> participantsGroup;
  @Nullable
  private VAComboBox<Long> courseComboBox;

  public AssignToCourseDialog(@Nonnull final CourseService courseService,
                              @Nonnull final ParticipantService participantService,
                              @Nonnull final MandantSettingService mandantSettingService) {
    this.courseService = courseService;
    this.participantService = participantService;
    this.binder = new Binder<>(AssignParticipantsToCourseDto.class);
    this.binder.setBean(new AssignParticipantsToCourseDto());

    this.entryFactory = new EntryFactory<>();
    this.filterFactory = new FilterEntryFactory<>();

    this.participantLabelCache = new ConcurrentHashMap<>();
    this.participantFilter = ParticipantFilter.emptyActive();

    final CallbackDataProvider<Long, ParticipantFilter> base = DataProvider.fromFilteringCallbacks(
            query -> {
              final ParticipantFilter filter = query.getFilter().orElse(ParticipantFilter.emptyActive());

              if (mandantSettingService.getSingleMandantSetting().isEnforceCourseLevelSettings() &&
                      courseComboBox != null &&
                      courseComboBox.getValue() != null) {
                final var selectedCourse = courseService.getCourseById(courseComboBox.getValue());
                filter.setCourseLevelIds(selectedCourse.getCourseLevels().stream()
                        .map(BaseDto::getId)
                        .collect(Collectors.toSet()));
              }

              final int offset = query.getOffset();
              final int limit = query.getLimit();

              final var dtos = participantService.findParticipants(filter, offset, limit, List.of());
              dtos.forEach(p -> participantLabelCache.put(p.getId(), p.getFullName()));
              return dtos.stream().map(ParticipantDto::getId);
            },
            query -> {
              final ParticipantFilter filter = query.getFilter().orElse(ParticipantFilter.emptyActive());
              return participantService.countParticipants(filter);
            }
    );
    this.participantDataProvider = base.withConfigurableFilter();
    this.participantDataProvider.setFilter(this.participantFilter);

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
    this.courseComboBox = createCourseComboBox();

    return createLayoutFromComponents(title, courseComboBox);
  }

  @Nonnull
  private VerticalLayout createParticipantLayout() {
    final var title = createTitleSpan("Participants");
    final var filterBar = new VAFilterBar();
    filterBar.setSearchHandler(this::setFilter);
    final var participants = createParticipantsCheckboxGroup();

    final var layout = createLayoutFromComponents(title, filterBar, participants);
    layout.setFlexGrow(1, participants);
    return layout;
  }

  private void setFilter(@Nullable final String searchText) {
    participantFilter = ParticipantFilter.fromSearchTextAndActive(searchText, true);
    participantDataProvider.setFilter(participantFilter);
    participantDataProvider.refreshAll();
  }

  @Nonnull
  private Span createTitleSpan(@Nonnull final String titleLabel) {
    final var title = new Span(titleLabel);
    title.addClassNames("va-required");
    title.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.SEMIBOLD);
    return title;
  }

  @Nonnull
  private Button createSaveButton() {
    final var button = new Button("Save");
    button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    button.setEnabled(false);
    button.addClickListener(event -> save());
    return button;
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
    final var participants = binder.getBean().getParticipants();
    final var course = binder.getBean().getCourse();

    participants.forEach(participant -> {
      participant.setCourse(course);
      participantService.updateParticipant(participant);
    });

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
    final var courseComboBox = entryFactory.createComboBoxEntry(
            dto -> dto.getCourse().getId(),
            (dto, value) -> dto.setCourse(value != null ?
                    courseService.getCourseById(value) :
                    CourseDto.empty()),
            binder,
            Optional.of("Select Course"),
            Publ.EMPTY_STRING,
            courseService.getAllCourses().stream()
                    .collect(Collectors.toMap(CourseDto::getId, CourseDto::getTitle))
    );
    courseComboBox.addValueChangeListener(e -> {
      updateSaveEnabled();
      setFilter(participantFilter.getSearchText());
    });
    return courseComboBox;
  }

  @Nonnull
  private Scroller createParticipantsCheckboxGroup() {
    participantsGroup = filterFactory.createCheckboxGroupEntry(
            dto -> dto.getParticipants().stream().map(BaseDto::getId).collect(Collectors.toSet()),
            (dto, value) -> {
              final var selectedParticipants = value.stream()
                      .map(participantService::getParticipantById)
                      .collect(Collectors.toSet());
              dto.setParticipants(selectedParticipants);
            },
            binder,
            Optional.of("Select Participants"),
            Publ.EMPTY_STRING,
            participantDataProvider,
            id -> participantLabelCache.getOrDefault(id, "Participant #" + id)
    );

    participantsGroup.addValueChangeListener(e -> updateSaveEnabled());
    participantsGroup.addClassName("participants-group");
    participantsGroup.setWidthFull();

    final var scroller = new Scroller();
    scroller.setWidthFull();
    scroller.setHeightFull();
    scroller.setContent(participantsGroup);
    return scroller;
  }
}
