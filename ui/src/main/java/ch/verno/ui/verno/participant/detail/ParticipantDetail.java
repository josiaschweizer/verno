package ch.verno.ui.verno.participant.detail;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.CourseLevelDto;
import ch.verno.common.db.dto.table.GenderDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.common.db.service.*;
import ch.verno.publ.Publ;
import ch.verno.ui.base.components.badge.VABadgeLabel;
import ch.verno.ui.base.components.badge.VABadgeLabelOptions;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.factory.BadgeLabelFactory;
import ch.verno.ui.base.pages.detail.BaseDetailView;
import ch.verno.common.lib.Routes;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@PermitAll
@Route(Routes.PARTICIPANTS + Routes.DETAIL)
@Menu(order = 1.1, icon = "vaadin:user", title = "participant.participant.detail")
public class ParticipantDetail extends BaseDetailView<ParticipantDto> implements HasDynamicTitle {

  @Nonnull private final IParticipantService participantService;
  @Nonnull private final IGenderService genderService;
  @Nonnull private final ICourseLevelService courseLevelService;
  @Nonnull private final ICourseService courseService;
  @Nonnull private final ITenantSettingService tenantSettingService;

  public ParticipantDetail(@Nonnull final IParticipantService participantService,
                           @Nonnull final IGenderService genderService,
                           @Nonnull final ICourseLevelService courseLevelService,
                           @Nonnull final ICourseService courseService,
                           @Nonnull final ITenantSettingService tenantSettingService) {
    this.participantService = participantService;
    this.genderService = genderService;
    this.courseLevelService = courseLevelService;
    this.courseService = courseService;
    this.tenantSettingService = tenantSettingService;

    super.setShowPaddingAroundDetail(true);
  }

  @Nullable
  @Override
  //todo fertig machen
  protected VABadgeLabel getInfoBadge() {
    final var active = getBinder().getBean().isActive();
    final var labelText = active ? getTranslation("shared.active") : getTranslation("shared.inactive");
    final var badgeOption = active ? VABadgeLabelOptions.SUCCESS : VABadgeLabelOptions.WARNING;

    final var badge = BadgeLabelFactory.createToolbarInfoBadgeLabel(labelText, badgeOption);
    return null;
  }

  @Nullable
  @Override
  protected Component getToolbarContextMenu() {
    final var contextButton = new Button(VaadinIcon.ELLIPSIS_DOTS_V.create());
    contextButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    final var contextMenu = new ContextMenu();
    contextMenu.setTarget(contextButton);
    contextMenu.setOpenOnClick(true);

    final MenuItem toggleActiveItem = contextMenu.addItem(Publ.EMPTY_STRING);
    toggleActiveItem.addClickListener(e -> {
      final var bean = getBinder().getBean();
      if (bean == null) {
        return;
      }

      final var updated = participantService
              .disableParticipant(bean, bean.isActive());

      getBinder().setBean(updated);
      getBinder().validate();
      applyFormMode(getFormModeByBean(updated));

      toggleActiveItem.setText(getToggleText(updated));
    });

    toggleActiveItem.setText(getToggleText(getBinder().getBean()));

    final var wrapper = new HorizontalLayout(contextButton, contextMenu);
    wrapper.setPadding(false);
    wrapper.setSpacing(false);
    wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
    return wrapper;
  }

  @Nonnull
  private String getToggleText(@Nullable ParticipantDto p) {
    if (p != null && p.isActive()) {
      return getTranslation("participant.disable.participant");
    }
    return getTranslation("participant.enable.participant");
  }
  @Nonnull
  @Override
  protected String getDetailPageName() {
    return getTranslation("participant.participant");
  }

  @Nonnull
  @Override
  protected String getDetailRoute() {
    return Routes.createUrlFromUrlSegments(Routes.PARTICIPANTS, Routes.DETAIL);
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

  @NonNull
  @Override
  public FormMode getFormModeByBean(@NonNull final ParticipantDto bean) {
    if (!bean.isActive()) {
      return FormMode.VIEW;
    }
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
    final var courseLevelsEntry = entryFactory.createMultiSelectComboBoxEntry(
            ParticipantDto::getCourseLevels,
            ParticipantDto::setCourseLevels,
            getBinder(),
            Optional.empty(),
            getTranslation("courseLevel.course_levels"),
            courseLevels,
            CourseLevelDto::displayName
    );

    final var courses = new ArrayList<CourseDto>();
    if (tenantSettingService.getCurrentTenantSettingOrDefault().isLimitCourseAssignmentsToActive()) {
      courses.addAll(courseService.getCoursesByCourseScheduleStatus(CourseScheduleStatus.PLANNED));
      courses.addAll(courseService.getCoursesByCourseScheduleStatus(CourseScheduleStatus.ACTIVE));
    } else {
      courses.addAll(courseService.getAllCourses());
    }

    final var coursesEntry = entryFactory.createMultiSelectComboBoxEntry(
            ParticipantDto::getCourses,
            ParticipantDto::setCourses,
            getBinder(),
            Optional.empty(),
            getTranslation(getTranslation("course.courses")),
            courses,
            CourseDto::displayName
    );

    courseLevelsEntry.addValueChangeListener(e -> {
      if (tenantSettingService.getCurrentTenantSettingOrDefault().isEnforceCourseLevelSettings()) {
        final var selectedLevels = e.getValue();
        final var selectedCourse = coursesEntry.getValue();

        final var filteredCourses = courses.stream()
                .filter(course -> course.getCourseLevels().stream().anyMatch(selectedLevels::contains))
                .collect(Collectors.toSet());
        coursesEntry.setItems(filteredCourses);

        final var updatedSelectedCourses = selectedCourse.stream()
                .filter(filteredCourses::contains)
                .collect(Collectors.toSet());
        coursesEntry.setValue(updatedSelectedCourses);
      }
    });

    return createLayoutFromComponents(courseLevelsEntry, coursesEntry);
  }

  @Nonnull
  private HorizontalLayout createParticipantNoteLayout() {
    final var note = entryFactory.createTextAreaEntry(
            ParticipantDto::getNote,
            ParticipantDto::setNote,
            getBinder(),
            Optional.empty(),
            getTranslation("shared.note")
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
    final var parentOneLayout = createParentLayout(getTranslation("participant.parent_one"),
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

    final var parentTwoLayout = createParentLayout(getTranslation("participant.parent_two"),
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
            getTranslation("shared.first.name")
    );

    final var lastNameEntry = entryFactory.createTextEntry(
            lastNameGetter,
            lastNameSetter,
            getBinder(),
            Optional.empty(),
            getTranslation("shared.last.name")
    );

    final var genderEntry = fieldFactory.createGenderField(
            genderGetter,
            genderSetter,
            getBinder(),
            genderService.getAllGenders()
    );

    final var emailEntry = entryFactory.createEmailEntry(
            emailGetter,
            emailSetter,
            getBinder(),
            Optional.empty(),
            getTranslation("shared.e.mail")
    );

    final var phoneEntry = entryFactory.createPhoneNumberEntry(
            phoneGetter,
            phoneSetter,
            getBinder(),
            Optional.empty(),
            getTranslation("shared.phone")
    );

    return new VerticalLayout(
            title,
            createLayoutFromComponents(firstNameEntry, lastNameEntry),
            genderEntry,
            emailEntry,
            phoneEntry
    );
  }

  @Override
  public String getPageTitle() {
    return getTranslation("participant.participant.detail");
  }
}