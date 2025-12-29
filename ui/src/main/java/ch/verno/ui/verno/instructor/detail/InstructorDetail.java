package ch.verno.ui.verno.instructor.detail;

import ch.verno.common.db.dto.InstructorDto;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.GenderService;
import ch.verno.server.service.InstructorService;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.detail.BaseDetailView;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;
import org.jspecify.annotations.NonNull;

@PermitAll
@Route(Routes.INSTRUCTORS + Routes.DETAIL)
@PageTitle("Participants Detail View")
@Menu(order = 2.1, icon = "vaadin:academy-cap", title = "Instructor Detail")
public class InstructorDetail extends BaseDetailView<InstructorDto> {

  @Nonnull
  private final InstructorService instructorService;
  @Nonnull
  private final GenderService genderService;

  public InstructorDetail(@Nonnull final InstructorService instructorService,
                          @Nonnull final GenderService genderService) {
    this.instructorService = instructorService;
    this.genderService = genderService;

    init();
  }

  @Nonnull
  @Override
  protected String getDetailPageName() {
    return VernoConstants.INSTRUCTOR;
  }

  @Nonnull
  @Override
  protected String getBasePageRoute() {
    return Routes.INSTRUCTORS;
  }

  @Nonnull
  @Override
  protected Binder<InstructorDto> createBinder() {
    return new Binder<>(InstructorDto.class);
  }

  @Nonnull
  @Override
  protected InstructorDto createBean(@Nonnull final InstructorDto bean) {
    return instructorService.createInstructor(bean);
  }

  @Nonnull
  @Override
  protected InstructorDto updateBean(@Nonnull final InstructorDto bean) {
    return instructorService.updateInstructor(bean);
  }

  @Nonnull
  @Override
  protected FormMode getDefaultFormMode() {
    return FormMode.EDIT;
  }

  @Nonnull
  @Override
  protected InstructorDto newBeanInstance() {
    return new InstructorDto();
  }

  @Override
  protected InstructorDto getBeanById(@Nonnull final Long id) {
    return instructorService.getInstructorById(id);
  }

  @Override
  protected void initUI() {
    final var instructorLayout = createInstructorLayout();
    final var addressLayout = createAddressLayout();
    add(instructorLayout, addressLayout);
  }

  private VerticalLayout createInstructorLayout() {
    final var layout = new VerticalLayout();
    layout.add(createInstructorInfoLayout());
    layout.add(createInstructorContactLayout());
    return layout;
  }

  @Nonnull
  private HorizontalLayout createInstructorInfoLayout() {
    final var firstname = fieldFactory.createFirstNameField(
            InstructorDto::getFirstName,
            InstructorDto::setFirstName,
            getBinder());
    final var lastname = fieldFactory.createLastNameField(
            InstructorDto::getLastName,
            InstructorDto::setLastName,
            getBinder());
    final var gender = fieldFactory.createGenderField(
            InstructorDto::getGender,
            InstructorDto::setGender,
            getBinder(),
            genderService.getAllGenders());

    return createLayoutFromComponents(firstname, lastname, gender);
  }

  @Nonnull
  private HorizontalLayout createInstructorContactLayout() {
    final var email = fieldFactory.createEmailField(
            InstructorDto::getEmail,
            InstructorDto::setEmail,
            getBinder());
    final var phone = fieldFactory.createPhoneNumberField(
            InstructorDto::getPhone,
            InstructorDto::setPhone,
            getBinder());
    return createLayoutFromComponents(email, phone);
  }

  @Nonnull
  private VerticalLayout createAddressLayout() {
    final var street = fieldFactory.createStreetField(
            instructorDto -> instructorDto.getAddress().getStreet(),
            (dto, value) -> dto.getAddress().setStreet(value),
            getBinder());
    final var houseNumber = fieldFactory.createHouseNumberField(
            instructorDto -> instructorDto.getAddress().getHouseNumber(),
            (dto, value) -> dto.getAddress().setHouseNumber(value),
            getBinder());
    final var zipCode = fieldFactory.createZipCodeField(
            instructorDto -> instructorDto.getAddress().getZipCode(),
            (dto, value) -> dto.getAddress().setZipCode(value),
            getBinder());
    final var city = fieldFactory.createCityField(
            instructorDto -> instructorDto.getAddress().getCity(),
            (dto, value) -> dto.getAddress().setCity(value),
            getBinder());
    final var country = fieldFactory.createCountryField(
            instructorDto -> instructorDto.getAddress().getCountry(),
            (dto, value) -> dto.getAddress().setCountry(value),
            getBinder());

    return new VerticalLayout(createLayoutFromComponents(street, houseNumber, zipCode, city, country));
  }
}
