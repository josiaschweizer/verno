package ch.verno.ui.verno.participant.detail;


import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.common.db.dto.GenderDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.server.service.CourseLevelService;
import ch.verno.server.service.CourseService;
import ch.verno.server.service.GenderService;
import ch.verno.server.service.ParticipantService;
import ch.verno.ui.base.components.toolbar.ViewToolbar;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Optional;
import java.util.stream.Collectors;

@Route("participants/detail")
@PageTitle("Participants Detail View")
public class ParticipantsDetail extends VerticalLayout implements HasUrlParameter<Long> {

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
    this.entryFactory = new EntryFactory<>();

    init();
  }

  private void init() {
    final var participantLayout = new VerticalLayout();
    participantLayout.setWidthFull();
    participantLayout.add(createParticipantInfoLayout());
    participantLayout.add(createParticipantContactLayout());
    participantLayout.add(createParticipantCourseLayout());

    final var addressLayout = new VerticalLayout(createAddressLayout());
    final var parentsLayout = createParentsLayout();

    setSizeFull();
    setPadding(false);
    setSpacing(false);

    add(new ViewToolbar("Participant Detail"));
    add(participantLayout, addressLayout, parentsLayout);

    final var saveButton = new Button("Save");
    saveButton.addClickListener(event -> {
      UI.getCurrent().navigate(Routes.PARTICIPANTS);
    });

    final var saveLayout = new HorizontalLayout(saveButton);
    saveLayout.setWidthFull();
    saveLayout.setJustifyContentMode(JustifyContentMode.END);

    add(new VerticalLayout(saveLayout));
  }

  @Nonnull
  private HorizontalLayout createParticipantInfoLayout() {
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

    final var genderEntry = entryFactory.createGenderEntry(
        ParticipantDto::getGender,
        ParticipantDto::setGender,
        binder,
        genderService.getAllGenders(),
        GenderDto::name,
        Optional.empty(),
        "Gender"
    );

    return createLayoutFromComponents(firstNameEntry, lastNameEntry, birthdateEntry, genderEntry);
  }

  @Nonnull
  private HorizontalLayout createParticipantContactLayout() {
    final var emailEntry = entryFactory.createEmailEntry(
        ParticipantDto::getEmail,
        ParticipantDto::setEmail,
        binder,
        Optional.empty(),
        "Email address"
    );
    final var phoneEntry = entryFactory.createPhoneNumberEntry(
        ParticipantDto::getPhone,
        ParticipantDto::setPhone,
        binder,
        Optional.empty(),
        "Phone number"
    );

    return createLayoutFromComponents(emailEntry, phoneEntry);
  }

  @Nonnull
  private HorizontalLayout createParticipantCourseLayout() {
    final var courseLevels = courseLevelService.getAllCourseLevels();
    final var courseLevelOptions = courseLevels.stream()
        .collect(Collectors.toMap(CourseLevelDto::id, CourseLevelDto::name));

    final var courseLevelEntry = entryFactory.createComboBoxEntry(
        dto -> dto.getCourseLevel().id(),
        (dto, levelId) -> dto.setCourseLevel(levelId == null ? CourseLevelDto.empty() : courseLevelService.getCourseLevelById(levelId)),
        binder,
        Optional.empty(),
        "Course Level",
        courseLevelOptions
    );

    final var courses = courseService.getAllCourses();
    final var courseOptions = courses.stream()
        .collect(Collectors.toMap(CourseDto::id, CourseDto::title));

    final var courseEntry = entryFactory.createComboBoxEntry(
        dto -> dto.getCourse() == null ? null : dto.getCourse().id(),
        (dto, courseId) -> dto.setCourse(courseId == null ? CourseDto.empty() : courseService.getCourseById(courseId)),
        binder,
        Optional.empty(),
        "Course",
        courseOptions
    );

    courseLevelEntry.addValueChangeListener(e -> {
      final var selectedLevelId = e.getValue();

      final var currentValue = courseEntry.getValue();
      courseEntry.clear();

      if (selectedLevelId == null) {
        courseEntry.setItems(courseOptions.keySet());
        courseEntry.setValue(currentValue);
        return;
      }

      final var filteredCourses = courses.stream()
          .filter(c -> !c.level().isEmpty() && selectedLevelId.equals(c.level().id()))
          .toList();

      final var filteredCourseOptions = filteredCourses.stream()
          .collect(Collectors.toMap(
              CourseDto::id,
              CourseDto::displayName
          ));

      courseEntry.setItems(filteredCourseOptions.keySet());

      if (!filteredCourseOptions.isEmpty()) {
        courseEntry.setValue(filteredCourseOptions.keySet().iterator().next());
      }
    });

    if (!courseLevelOptions.isEmpty()) {
      courseLevelEntry.setValue(courseLevelOptions.keySet().iterator().next());
    }

    return createLayoutFromComponents(courseLevelEntry, courseEntry);
  }

  @Nonnull
  private HorizontalLayout createAddressLayout() {
    final var streetEntry = entryFactory.createTextEntry(
        dto -> dto.getAddress().getStreet(),
        (dto, street) -> dto.getAddress().setStreet(street),
        binder,
        Optional.empty(),
        "Street"
    );
    final var houseNumberEntry = entryFactory.createTextEntry(
        dto -> dto.getAddress().getHouseNumber(),
        (dto, houseNumber) -> dto.getAddress().setHouseNumber(houseNumber),
        binder,
        Optional.empty(),
        "House Number"
    );
    final var zipCodeEntry = entryFactory.createTextEntry(
        dto -> dto.getAddress().getZipCode(),
        (dto, zipCode) -> dto.getAddress().setZipCode(zipCode),
        binder,
        Optional.empty(),
        "ZIP Code"
    );
    final var cityEntry = entryFactory.createTextEntry(
        dto -> dto.getAddress().getCity(),
        (dto, city) -> dto.getAddress().setCity(city),
        binder,
        Optional.empty(),
        "City"
    );
    final var countryEntry = entryFactory.createTextEntry(
        dto -> dto.getAddress().getCountry(),
        (dto, country) -> dto.getAddress().setCountry(country),
        binder,
        Optional.empty(),
        "Country"
    );

    return createLayoutFromComponents(streetEntry, houseNumberEntry, zipCodeEntry, cityEntry, countryEntry);
  }

  @Nonnull
  private HorizontalLayout createParentsLayout() {
    final var parentOneLayout = createParentLayout("Parent One",
        participantDto -> participantDto.getParentOne().getFirstName(),
        (participantDto, firstname) -> participantDto.getParentOne().setFirstName(firstname),
        participantDto -> participantDto.getParentOne().getLastName(),
        (participantDto, lastName) -> participantDto.getParentOne().setLastName(lastName),
        participantDto -> participantDto.getParentOne().getGender(),
        (participantDto, gender) -> participantDto.getParentOne().setGender(gender),
        participantDto -> participantDto.getParentOne().getEmail(),
        (participantDto, email) -> participantDto.getParentOne().setEmail(email),
        participantDto -> participantDto.getParentOne().getPhoneNumber(),
        (participantDto, phoneNumber) -> participantDto.getParentOne().setPhoneNumber(phoneNumber)
    );

    final var parentTwoLayout = createParentLayout("Parent Two",
        participantDto -> participantDto.getParentTwo().getFirstName(),
        (participantDto, firstname) -> participantDto.getParentTwo().setFirstName(firstname),
        participantDto -> participantDto.getParentTwo().getLastName(),
        (participantDto, lastName) -> participantDto.getParentTwo().setLastName(lastName),
        participantDto -> participantDto.getParentTwo().getGender(),
        (participantDto, gender) -> participantDto.getParentTwo().setGender(gender),
        participantDto -> participantDto.getParentTwo().getEmail(),
        (participantDto, email) -> participantDto.getParentTwo().setEmail(email),
        participantDto -> participantDto.getParentTwo().getPhoneNumber(),
        (participantDto, phoneNumber) -> participantDto.getParentTwo().setPhoneNumber(phoneNumber)
    );


    return createLayoutFromComponents(parentOneLayout, parentTwoLayout);
  }

  @Nonnull
  private VerticalLayout createParentLayout(@Nonnull final String title,
                                            @Nonnull final ValueProvider<ParticipantDto, String> firstNameGetter,
                                            @Nonnull final Setter<ParticipantDto, String> firstNameSetter,
                                            @Nonnull final ValueProvider<ParticipantDto, String> lastNameGetter,
                                            @Nonnull final Setter<ParticipantDto, String> lastNameSetter,
                                            @Nonnull final ValueProvider<ParticipantDto, GenderDto> genderGetter,
                                            @Nonnull final Setter<ParticipantDto, GenderDto> genderSetter,
                                            @Nonnull final ValueProvider<ParticipantDto, String> emailGetter,
                                            @Nonnull final Setter<ParticipantDto, String> emailSetter,
                                            @Nonnull final ValueProvider<ParticipantDto, PhoneNumber> phoneGetter,
                                            @Nonnull final Setter<ParticipantDto, PhoneNumber> phoneSetter) {
    final var firstNameEntry = entryFactory.createTextEntry(
        firstNameGetter,
        firstNameSetter,
        binder,
        Optional.empty(),
        "First Name"
    );

    final var lastNameEntry = entryFactory.createTextEntry(
        lastNameGetter,
        lastNameSetter,
        binder,
        Optional.empty(),
        "Last Name"
    );
    final var nameLayout = createLayoutFromComponents(firstNameEntry, lastNameEntry);

    final var genderEntry = entryFactory.createGenderEntry(
        genderGetter,
        genderSetter,
        binder,
        genderService.getAllGenders(),
        GenderDto::name,
        Optional.empty(),
        "Gender"
    );

    final var emailEntry = entryFactory.createEmailEntry(
        emailGetter,
        emailSetter,
        binder,
        Optional.empty(),
        "Email address"
    );

    final var phoneEntry = entryFactory.createPhoneNumberEntry(
        phoneGetter,
        phoneSetter,
        binder,
        Optional.empty(),
        "Phone number"
    );

    return new VerticalLayout(
        createTitle(title),
        nameLayout,
        genderEntry,
        emailEntry,
        phoneEntry
    );
  }

  @Nonnull
  private H1 createTitle(@Nonnull final String titleString) {
    final var title = new H1(titleString);
    title.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.NONE, LumoUtility.FontWeight.LIGHT, LumoUtility.Border.BOTTOM);
    return title;
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

  @Override
  public void setParameter(@Nonnull final BeforeEvent event,
                           @OptionalParameter @Nullable final Long parameter) {
    if (parameter == null) {
      binder.setBean(new ParticipantDto());
      return;
    }

    final var participant = participantService.getParticipantById(parameter);
    binder.setBean(participant);
  }
}
