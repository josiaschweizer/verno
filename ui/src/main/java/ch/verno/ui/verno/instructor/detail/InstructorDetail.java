package ch.verno.ui.verno.instructor.detail;

import ch.verno.common.lib.Routes;
import ch.verno.contract.dto.table.instructor.InstructorDto;
import ch.verno.contract.dto.table.setting.AppUserSettingDto;
import ch.verno.lib.Lazy;
import ch.verno.rpc.client.gender.GenderClient;
import ch.verno.rpc.client.instructor.InstructorClient;
import ch.verno.rpc.properties.user.UserProperties;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.components.layout.horizontal.VAHorizontalLayout;
import ch.verno.ui.base.components.layout.vertical.VAVerticalLayout;
import ch.verno.ui.lib.pages.detail.BaseDetailView;
import ch.verno.ui.lib.url.RoutesUtil;
import ch.verno.ui.lib.util.LayoutUtil;
import com.google.inject.Injector;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.util.Optional;

@PermitAll
@Route(Routes.INSTRUCTORS + Routes.DETAIL)
@Menu(order = 2.1, icon = "vaadin:academy-cap", title = "shared.instructor.detail")
public class InstructorDetail extends BaseDetailView<InstructorDto> implements HasDynamicTitle {

  @Nonnull private final Lazy<GenderClient> genderClient;
  @Nonnull private final Lazy<InstructorClient> instructorClient;

  @Nonnull private final AppUserSettingDto userSettingDto;

  public InstructorDetail(@Nonnull final Injector injector) {
    super(injector);

    this.genderClient = Lazy.of(() -> injector.getInstance(GenderClient.class));
    this.instructorClient = Lazy.of(() -> injector.getInstance(InstructorClient.class));

    this.userSettingDto = injector.getInstance(UserProperties.class).getCurrentAppUserSetting();
    setShowPaddingAroundDetail(true);
  }

  @Nonnull
  @Override
  protected String getDetailPageName() {
    return getTranslation("shared.instructor");
  }

  @Nonnull
  @Override
  protected String getDetailRoute() {
    return RoutesUtil.createUrlFromUrlSegments(Routes.INSTRUCTORS, Routes.DETAIL);
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

  @Override
  protected void createBean(@Nonnull final InstructorDto bean) {
    instructorClient.get().saveInstructor(bean);
  }

  @Override
  protected void updateBean(@Nonnull final InstructorDto bean) {
    instructorClient.get().saveInstructor(bean);
  }

  @Nonnull
  @Override
  protected FormMode getDefaultFormMode() {
    return FormMode.EDIT;
  }

  @Nonnull
  @Override
  protected InstructorDto newBeanInstance() {
    return InstructorDto.empty();
  }

  @Nonnull
  @Override
  protected Optional<InstructorDto> getBeanById(@Nonnull final Long id) {
    return instructorClient.get().getInstructorById(id);
  }

  @Override
  protected void initUI() {
    final var instructorLayout = createInstructorLayout();
    final var addressLayout = createAddressLayout();
    add(instructorLayout, addressLayout);
  }

  @Nonnull
  private VAVerticalLayout createInstructorLayout() {
    final var layout = new VAVerticalLayout();
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
            genderClient.get().getAllGenders(),
            userSettingDto.getLanguage()
    );

    return LayoutUtil.createHorizontal(firstname, lastname, gender);
  }

  @Nonnull
  private VAHorizontalLayout createInstructorContactLayout() {
    final var email = fieldFactory.createEmailField(
            InstructorDto::getEmail,
            InstructorDto::setEmail,
            getBinder());
    final var phone = fieldFactory.createPhoneNumberField(
            InstructorDto::getPhone,
            InstructorDto::setPhone,
            getBinder());
    return LayoutUtil.createHorizontal(email, phone);
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

    return new VerticalLayout(LayoutUtil.createHorizontal(street, houseNumber, zipCode, city, country));
  }

  @Override
  public String getPageTitle() {
    return getTranslation("shared.instructor.detail");
  }
}
