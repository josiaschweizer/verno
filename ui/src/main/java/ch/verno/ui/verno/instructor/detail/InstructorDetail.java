package ch.verno.ui.verno.instructor.detail;

import ch.verno.common.db.dto.table.InstructorDto;
import ch.verno.common.db.service.IGenderService;
import ch.verno.common.db.service.IInstructorService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.Routes;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.pages.detail.BaseDetailView;
import ch.verno.ui.lib.util.LayoutUtil;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;
import org.jspecify.annotations.NonNull;

@PermitAll
@Route(Routes.INSTRUCTORS + Routes.DETAIL)
@Menu(order = 2.1, icon = "vaadin:academy-cap", title = "shared.instructor.detail")
public class InstructorDetail extends BaseDetailView<InstructorDto> implements HasDynamicTitle {

  @Nonnull
  private final IInstructorService instructorService;
  @Nonnull
  private final IGenderService genderService;

  public InstructorDetail(@Nonnull final GlobalInterface globalInterface) {
    super(globalInterface);
    this.instructorService = globalInterface.getService(IInstructorService.class);
    this.genderService = globalInterface.getService(IGenderService.class);

    super.setShowPaddingAroundDetail(true);
  }

  @Nonnull
  @Override
  protected String getDetailPageName() {
    return getTranslation("shared.instructor");
  }

  @NonNull
  @Override
  protected String getDetailRoute() {
    return Routes.createUrlFromUrlSegments(Routes.INSTRUCTORS, Routes.DETAIL);
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
  protected void createBean(@Nonnull final InstructorDto bean) {
    instructorService.createInstructor(bean);
  }

  @Nonnull
  @Override
  protected void updateBean(@Nonnull final InstructorDto bean) {
    instructorService.updateInstructor(bean);
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

    return LayoutUtil.createHorizontalLayoutFromComponents(firstname, lastname, gender);
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
    return LayoutUtil.createHorizontalLayoutFromComponents(email, phone);
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

    return new VerticalLayout(LayoutUtil.createHorizontalLayoutFromComponents(street, houseNumber, zipCode, city, country));
  }

  @Override
  public String getPageTitle() {
    return getTranslation("shared.instructor.detail");
  }
}
