package ch.verno.ui.verno;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.GenderDto;
import ch.verno.ui.base.components.entry.phonenumber.PhoneEntry;
import ch.verno.ui.base.components.entry.twooption.VATwoOptionEntry;
import ch.verno.ui.base.factory.EntryFactory;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class FieldFactory<T> {

  @Nonnull
  private final EntryFactory<T, GenderDto> entryFactory;

  public FieldFactory(@Nonnull final EntryFactory<T, GenderDto> entryFactory) {
    this.entryFactory = entryFactory;
  }

  @Nonnull
  public TextField createFirstNameField(@Nonnull final ValueProvider<T, String> valueProvider,
                                        @Nonnull final Setter<T, String> valueSetter,
                                        @Nonnull final Binder<T> binder) {
    return entryFactory.createTextEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.of("First name is required"),
            "First Name");
  }

  @Nonnull
  public TextField createLastNameField(@Nonnull final ValueProvider<T, String> valueProvider,
                                       @Nonnull final Setter<T, String> valueSetter,
                                       @Nonnull final Binder<T> binder) {
    return entryFactory.createTextEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.of("Last Name is required"),
            "Last Name"
    );
  }

  @Nonnull
  public DatePicker createBirthDateField(@Nonnull final ValueProvider<T, LocalDate> valueProvider,
                                         @Nonnull final Setter<T, LocalDate> valueSetter,
                                         @Nonnull final Binder<T> binder) {
    final var entry = entryFactory.createDateEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.empty(),
            "Birthdate"
    );
    entry.setWidthFull();
    return entry;
  }

  @Nonnull
  public VATwoOptionEntry<GenderDto> createGenderField(@Nonnull final ValueProvider<T, GenderDto> valueProvider,
                                                       @Nonnull final Setter<T, GenderDto> valueSetter,
                                                       @Nonnull final Binder<T> binder,
                                                       @Nonnull final List<GenderDto> genderOptions) {
    return entryFactory.createGenderEntry(
            valueProvider,
            valueSetter,
            binder,
            genderOptions,
            GenderDto::getName,
            Optional.empty(),
            "Gender"
    );
  }

  @Nonnull
  public EmailField createEmailField(@Nonnull final ValueProvider<T, String> valueProvider,
                                     @Nonnull final Setter<T, String> valueSetter,
                                     @Nonnull final Binder<T> binder) {
    return entryFactory.createEmailEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.empty(),
            "Email address"
    );
  }

  @Nonnull
  public PhoneEntry createPhoneNumberField(@Nonnull final ValueProvider<T, PhoneNumber> valueProvider,
                                           @Nonnull final Setter<T, PhoneNumber> valueSetter,
                                           @Nonnull final Binder<T> binder) {
    return entryFactory.createPhoneNumberEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.empty(),
            "Phone number"
    );
  }

  @Nonnull
  public TextField createStreetField(@Nonnull final ValueProvider<T, String> valueProvider,
                                     @Nonnull final Setter<T, String> valueSetter,
                                     @Nonnull final Binder<T> binder) {
    return entryFactory.createTextEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.empty(),
            "Street"
    );
  }

  @Nonnull
  public TextField createHouseNumberField(@Nonnull final ValueProvider<T, String> valueProvider,
                                          @Nonnull final Setter<T, String> valueSetter,
                                          @Nonnull final Binder<T> binder) {
    return entryFactory.createTextEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.empty(),
            "House Number"
    );
  }

  @Nonnull
  public TextField createZipCodeField(@Nonnull final ValueProvider<T, String> valueProvider,
                                      @Nonnull final Setter<T, String> valueSetter,
                                      @Nonnull final Binder<T> binder) {
    return entryFactory.createTextEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.empty(),
            "ZIP Code"
    );
  }

  @Nonnull
  public TextField createCityField(@Nonnull final ValueProvider<T, String> valueProvider,
                                   @Nonnull final Setter<T, String> valueSetter,
                                   @Nonnull final Binder<T> binder) {
    return entryFactory.createTextEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.empty(),
            "City"
    );
  }

  @Nonnull
  public TextField createCountryField(@Nonnull final ValueProvider<T, String> valueProvider,
                                      @Nonnull final Setter<T, String> valueSetter,
                                      @Nonnull final Binder<T> binder) {
    return entryFactory.createTextEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.empty(),
            "Country"
    );
  }

  @Nonnull
  public ComboBox<Long> createCourseLevelField(@Nonnull final ValueProvider<T, Long> valueProvider,
                                             @Nonnull final Setter<T, Long> valueSetter,
                                             @Nonnull final Binder<T> binder,
                                             @Nonnull final  java.util.Map<Long, String> courseLevelOptions) {
    return entryFactory.createComboBoxEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.empty(),
            "Course Level",
            courseLevelOptions
    );
  }
}
