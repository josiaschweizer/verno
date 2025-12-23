package ch.verno.ui.verno.participant.detail;

import ch.verno.common.db.mapper.GenderMapper;
import ch.verno.common.db.service.CourseLevelService;
import ch.verno.common.db.service.CourseService;
import ch.verno.common.db.service.GenderService;
import ch.verno.common.db.service.ParticipantService;
import ch.verno.server.entity.CourseLevelEntity;
import ch.verno.ui.base.components.toolbar.ViewToolbar;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.verno.participant.dto.GenderDto;
import ch.verno.ui.verno.participant.dto.ParticipantDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;

import java.util.Optional;
import java.util.stream.Collectors;

@Route("participants/detail")
@PageTitle("Participants Detail View")
public class ParticipantsDetail extends VerticalLayout {

  @Nonnull
  private final ParticipantService participantService;
  @Nonnull
  private final GenderService genderService;
  @Nonnull
  private final CourseLevelService courseLevelService;
  @Nonnull
  private final CourseService courseService;
  @Nonnull
  private final Binder<ParticipantDto> binder;
  @Nonnull
  private final ParticipantDto participant;
  @Nonnull
  private final EntryFactory<ParticipantDto, GenderDto> entryFactory;

  public ParticipantsDetail(@Nonnull final ParticipantService participantService,
                            @Nonnull final GenderService genderService,
                            @Nonnull final CourseLevelService courseLevelService,
                            @Nonnull final CourseService courseService) {
    this.participantService = participantService;
    this.genderService = genderService;
    this.courseLevelService = courseLevelService;
    this.courseService = courseService;

    this.binder = new Binder<>(ParticipantDto.class);
    this.participant = new ParticipantDto();
    this.entryFactory = new EntryFactory<>();


    init();
  }

  private void init() {
    final var saveButton = new Button("Save");
    saveButton.addClickListener(event -> {
      UI.getCurrent().navigate("participants");
    });

    final var participantLayout = new VerticalLayout();
    participantLayout.setWidthFull();
    participantLayout.add(createFirstLayer());
    participantLayout.add(createSecondLayer());
    participantLayout.add(createThirdLayer());

    setSizeFull();
    setPadding(false);
    setSpacing(false);

    add(new ViewToolbar("Participant Detail", saveButton));
    add(participantLayout);
  }

  @Nonnull
  private HorizontalLayout createFirstLayer() {
    final var firstNameEntry = entryFactory.createTextEntry(
        ParticipantDto::getFirstName,
        ParticipantDto::setFirstName,
        binder,
        Optional.of("First Name is required"),
        "First Name"
    );

    final var lastNameEntry = entryFactory.createTextEntry(
        ParticipantDto::getLastName,
        ParticipantDto::setLastName,
        binder,
        Optional.of("Last Name is required"),
        "Last Name"
    );

    final var birthdateEntry = entryFactory.createDateEntry(
        ParticipantDto::getBirthdate,
        ParticipantDto::setBirthdate,
        binder,
        Optional.empty(),
        "Birthdate");
    birthdateEntry.setWidthFull();

    final var genderEntry = entryFactory.createTwoOptionEntry(
        ParticipantDto::getGender,
        ParticipantDto::setGender,
        binder,
        genderService.getAllGenders().stream().map(GenderMapper::toDto).toList(),
        GenderDto::name,
        Optional.empty(),
        "Gender");
    genderEntry.setWidthFull();


    return createLayoutFromComponents(firstNameEntry, lastNameEntry, birthdateEntry, genderEntry);
  }

  @Nonnull
  private HorizontalLayout createSecondLayer() {
    final var emailEntry = entryFactory.createEmailEntry(
        ParticipantDto::getEmail,
        ParticipantDto::setEmail,
        binder,
        Optional.empty(),
        "Email address"
    );
    final var phoneEntry = entryFactory.createPhoneNumberEntry(
        ParticipantDto::getPhoneNumber,
        ParticipantDto::setPhoneNumber,
        binder,
        Optional.empty(),
        "Phone number"
    );

    return createLayoutFromComponents(emailEntry, phoneEntry);
  }

  @Nonnull
  private HorizontalLayout createThirdLayer() {
    final var courseLevels = courseLevelService.getAllCourseLevels();
    final var options = courseLevels.stream()
        .collect(Collectors.toMap(
            CourseLevelEntity::getId,
            CourseLevelEntity::getName
        ));

    final var courseLevelEntry = entryFactory.createComboBoxEntry(
        ParticipantDto::getCourseLevelId,
        ParticipantDto::setCourseLevelId,
        binder,
        Optional.empty(),
        "Course Level",
        options
    );

    return createLayoutFromComponents(courseLevelEntry);
  }

  @Nonnull
  private HorizontalLayout createLayoutFromComponents(@Nonnull final Component... components) {
    final var layout = new HorizontalLayout();
    layout.setWidthFull();

    for (final var component : components) {
      layout.add(component);
    }

    return layout;

  }

}
