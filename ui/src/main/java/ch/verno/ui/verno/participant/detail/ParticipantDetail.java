package ch.verno.ui.verno.participant.detail;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.common.db.dto.GenderDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.CourseLevelService;
import ch.verno.server.service.CourseService;
import ch.verno.server.service.GenderService;
import ch.verno.server.service.ParticipantService;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.detail.BaseDetailView;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.util.Optional;
import java.util.stream.Collectors;

@PermitAll
@Route(Routes.PARTICIPANTS + Routes.DETAIL)
@PageTitle("Participants Detail View")
@Menu(order = 1.1, icon = "vaadin:user", title = "Participant Detail")
public class ParticipantDetail extends BaseDetailView<ParticipantDto> {

  @Nonnull
  private final ParticipantService participantService;
  @Nonnull
  private final GenderService genderService;
  @Nonnull
  private final CourseLevelService courseLevelService;
  @Nonnull
  private final CourseService courseService;

  public ParticipantDetail(@Nonnull final ParticipantService participantService,
                           @Nonnull final GenderService genderService,
                           @Nonnull final CourseLevelService courseLevelService,
                           @Nonnull final CourseService courseService) {
    this.participantService = participantService;
    this.genderService = genderService;
    this.courseLevelService = courseLevelService;
    this.courseService = courseService;

    init();
  }

  @Nonnull
  @Override
  protected String getDetailPageName() {
    return VernoConstants.PARTICIPANT;
  }

  @Nonnull
  @Override
  protected String getBasePageRoute() {
    return Routes.PARTICIPANTS;
  }

  @Nonnull
  @Override
  protected Binder<ParticipantDto> createBinder() {
    return new Binder<>(ParticipantDto.class);
  }

  @Nonnull
  @Override
  protected ParticipantDto createBean(@Nonnull final ParticipantDto bean) {
    return participantService.createParticipant(bean);
  }

  @Nonnull
  @Override
  protected ParticipantDto updateBean(@Nonnull final ParticipantDto bean) {
    return participantService.updateParticipant(bean);
  }

  @Nonnull
  @Override
  protected FormMode getDefaultFormMode() {
    return FormMode.EDIT;
  }

  @Nonnull
  @Override
  protected ParticipantDto newBeanInstance() {
    return new ParticipantDto();
  }

  @Override
  protected ParticipantDto getBeanById(@Nonnull final Long id) {
    return participantService.getParticipantById(id);
  }

  @Override
  protected void initUI() {
    final var participantLayout = createParticipantLayout();
    final var addressLayout = createAddressLayout();
    final var parentsLayout = createParentsLayout();

    add(participantLayout, addressLayout, parentsLayout);
  }

  @Nonnull
  private VerticalLayout createParticipantLayout() {
    final var participantLayout = new VerticalLayout();
    participantLayout.setWidthFull();
    participantLayout.add(createParticipantInfoLayout());
    participantLayout.add(createParticipantContactLayout());
    participantLayout.add(createParticipantCourseLayout());
    participantLayout.add(createParticipantNoteLayout());
    return participantLayout;
  }

  @Nonnull
  private HorizontalLayout createParticipantInfoLayout() {
    final var firstNameEntry = fieldFactory.createFirstNameField(
            ParticipantDto::getFirstName,
            ParticipantDto::setFirstName,
            getBinder());
    final var lastNameEntry = fieldFactory.createLastNameField(
            ParticipantDto::getLastName,
            ParticipantDto::setLastName,
            getBinder());
    final var birthdateEntry = fieldFactory.createBirthDateField(
            ParticipantDto::getBirthdate,
            ParticipantDto::setBirthdate,
            getBinder());
    final var genderEntry = fieldFactory.createGenderField(
            ParticipantDto::getGender,
            ParticipantDto::setGender,
            getBinder(),
            genderService.getAllGenders());

    return createLayoutFromComponents(firstNameEntry, lastNameEntry, birthdateEntry, genderEntry);
  }

  @Nonnull
  private HorizontalLayout createParticipantContactLayout() {
    final var emailEntry = fieldFactory.createEmailField(
            ParticipantDto::getEmail,
            ParticipantDto::setEmail,
            getBinder());
    final var phoneEntry = fieldFactory.createPhoneNumberField(
            ParticipantDto::getPhone,
            ParticipantDto::setPhone,
            getBinder());

    return createLayoutFromComponents(emailEntry, phoneEntry);
  }

  @Nonnull
  private HorizontalLayout createParticipantCourseLayout() {
    final var courseLevels = courseLevelService.getAllCourseLevels();
    final var courseLevelOptions = courseLevels.stream()
            .collect(Collectors.toMap(CourseLevelDto::getId, CourseLevelDto::getName));

    final var courseLevelEntry = fieldFactory.createCourseLevelField(
            dto -> dto.getCourseLevel().getId(),
            (dto, value) -> dto.setCourseLevel(value == null ?
                    CourseLevelDto.empty() :
                    courseLevelService.getCourseLevelById(value)),
            getBinder(),
            courseLevelOptions
    );

    final var courses = courseService.getAllCourses();
    final var courseOptions = courses.stream()
            .collect(Collectors.toMap(CourseDto::getId, CourseDto::getTitle));

    final var courseEntry = entryFactory.createComboBoxEntry(
            dto -> dto.getCourse() == null ? null : dto.getCourse().getId(),
            (dto, courseId) -> dto.setCourse(courseId == null ? CourseDto.empty() : courseService.getCourseById(courseId)),
            getBinder(),
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
              .filter(c -> c.getCourseLevels().stream()
                      .anyMatch(level -> selectedLevelId.equals(level.getId())))
              .toList();

      final var filteredCourseOptions = filteredCourses.stream()
              .collect(Collectors.toMap(CourseDto::getId, CourseDto::displayName));

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
  private HorizontalLayout createParticipantNoteLayout() {
    final var note = entryFactory.createTextAreaEntry(
            ParticipantDto::getNote,
            ParticipantDto::setNote,
            getBinder(),
            Optional.empty(),
            "Note"
    );

    return createLayoutFromComponents(note);
  }

  @Nonnull
  private VerticalLayout createAddressLayout() {
    final var streetEntry = fieldFactory.createStreetField(
            dto -> dto.getAddress().getStreet(),
            (dto, street) -> dto.getAddress().setStreet(street),
            getBinder());
    final var houseNumberEntry = fieldFactory.createHouseNumberField(
            dto -> dto.getAddress().getHouseNumber(),
            (dto, houseNumber) -> dto.getAddress().setHouseNumber(houseNumber),
            getBinder());
    final var zipCodeEntry = fieldFactory.createZipCodeField(
            dto -> dto.getAddress().getZipCode(),
            (dto, zipCode) -> dto.getAddress().setZipCode(zipCode),
            getBinder());
    final var cityEntry = fieldFactory.createCityField(
            dto -> dto.getAddress().getCity(),
            (dto, city) -> dto.getAddress().setCity(city),
            getBinder());
    final var countryEntry = fieldFactory.createCountryField(
            dto -> dto.getAddress().getCountry(),
            (dto, country) -> dto.getAddress().setCountry(country),
            getBinder());

    return new VerticalLayout(createLayoutFromComponents(streetEntry, houseNumberEntry, zipCodeEntry, cityEntry, countryEntry));
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
            participantDto -> participantDto.getParentOne().getPhone(),
            (participantDto, phoneNumber) -> participantDto.getParentOne().setPhone(phoneNumber)
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
            participantDto -> participantDto.getParentTwo().getPhone(),
            (participantDto, phoneNumber) -> participantDto.getParentTwo().setPhone(phoneNumber)
    );

    return createLayoutFromComponents(parentOneLayout, parentTwoLayout);
  }

  @Nonnull
  private VerticalLayout createParentLayout(@Nonnull final String titleString,
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
    final var title = new H1(titleString);
    title.addClassNames(
            LumoUtility.FontSize.XLARGE,
            LumoUtility.Margin.NONE,
            LumoUtility.FontWeight.LIGHT,
            LumoUtility.Border.BOTTOM
    );

    final var firstNameEntry = entryFactory.createTextEntry(
            firstNameGetter,
            firstNameSetter,
            getBinder(),
            Optional.empty(),
            "First Name"
    );

    final var lastNameEntry = entryFactory.createTextEntry(
            lastNameGetter,
            lastNameSetter,
            getBinder(),
            Optional.empty(),
            "Last Name"
    );

    final var genderEntry = entryFactory.createGenderEntry(
            genderGetter,
            genderSetter,
            getBinder(),
            genderService.getAllGenders(),
            GenderDto::getName,
            Optional.empty(),
            "Gender"
    );

    final var emailEntry = entryFactory.createEmailEntry(
            emailGetter,
            emailSetter,
            getBinder(),
            Optional.empty(),
            "Email address"
    );

    final var phoneEntry = entryFactory.createPhoneNumberEntry(
            phoneGetter,
            phoneSetter,
            getBinder(),
            Optional.empty(),
            "Phone number"
    );

    return new VerticalLayout(
            title,
            createLayoutFromComponents(firstNameEntry, lastNameEntry),
            genderEntry,
            emailEntry,
            phoneEntry
    );
  }
}